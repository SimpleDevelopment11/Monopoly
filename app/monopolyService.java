package app;

import java.util.ArrayList;

import Cards.*;
import gameSpaces.*;
import responses.buyResponse;
import responses.gameStateResponse;
import responses.outcomeResponse;
import responses.rollResponse;

public class monopolyService {

    public monopolyLogic logic;
    public mockServer server;
    private outcomeResponse outcome;

    public monopolyService()
    {
        this.server = new mockServer();
        this.logic = new monopolyLogic();
    }

    public Board getBoard()
    {
        return server.requestBoard();
    }

    public ArrayList<Player> getPlayers()
    {
        return this.server.getPlayers();
    }

    public Player addPlayer()
    {
        return this.server.addPlayer();
    }

    public rollResponse handleRoll(Player[] players)
    {
        if (server.gameOver)
        {
            return null;
        }
        Player findPlayer = null;
        int currentNum = server.currentPlayerNumber();
        for (Player player : players)
        {
            if (player.playerNumber == currentNum)
            {
                findPlayer = player;
                break;
            }
        }
        if (findPlayer != null)
        {
            this.outcome = new outcomeResponse();
            String message = "";
            int[] roll = logic.generateRoll();
            int combinedRoll = roll[0] + roll[1];
            server.currentRoll = combinedRoll;
            boolean inJail = getCurrentPlayer().isInJail;
            server.onDoubles = false;
            if (logic.isDoubles(roll))
            {
                message = "You rolled doubles!";
                if (inJail)
                {
                    server.roll(combinedRoll);
                    server.getCurrentPlayer().isInJail = false;
                    server.resetDoubles();
                    return new rollResponse(roll, message);
                }
                server.increaseDoubles();
                server.onDoubles = true;
                if (server.getDoublesCount() == server.doublesCount())
                {
                    message = server.doublesCount() + " doubles in a row. YOU MUST GO TO JAIL!!!";
                    server.resetDoubles();
                    server.goToJail();
                    server.onDoubles = false;
                    outcome.turnOver = true;
                }
                else
                {
                    server.roll(combinedRoll);
                }
            }
            else
            {
                if (inJail)
                {
                    message = "You failed to roll doubles so you will remain in jail.";
                    outcome.turnOver = true;
                }
                else
                {
                    server.roll(combinedRoll);
                }
                server.resetDoubles();
            }
            return new rollResponse(roll, message);
        }
        return null;
    }

    public outcomeResponse determineRollOutcome(){
        if (this.outcome.turnOver)
        {
            return this.outcome;
        }
        outcome = new outcomeResponse();
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.previousPosition > currentPlayer.position)
        {
            this.server.earnGoMoney();
        }
        boardSpace landedSpace = server.getLandedSpace();
        this.outcome.landedSpace = landedSpace;

        if (landedSpace instanceof Property)
        {
            Property propertySpace = (Property) landedSpace;
            if (propertySpace.ownedBy == null && canPay(propertySpace.initialCost))
            {
                this.outcome.canBuyProperty = true;
            }
            else if (propertySpace.ownedBy != null && propertySpace.ownedBy != getCurrentPlayer())
            {
                outcome.rentState = new outcomeResponse().new oweRent();
                outcome.rentState.oweOrNot = true;
                int rent = propertySpace.getRent(server.currentRoll);
                outcome.rentState.amountOwed = rent;
                Player owner = propertySpace.ownedBy;
                if (getCurrentPlayer().readyCash >= rent)
                {
                    getCurrentPlayer().readyCash -= rent;
                    owner.readyCash += rent;
                }
                else if(canPay(rent))
                {
                    server.setWaitingPayment(rent);
                    server.setWaitingPayee(owner);
                    outcome.rentState.needToMortgage = true;
                }
                else
                {
                    initBankruptcy(currentPlayer, owner);
                }
            }
        }
        else if (landedSpace instanceof chanceCommunitySpace)
        {
            chanceCommunitySpace deckSpace = (chanceCommunitySpace) landedSpace;

            Card drawnCard = null;
            cardQueue deck = null;
            if (deckSpace.deck == chanceCommunitySpace.deckType.COMMUNITYCHEST) {
                drawnCard = server.communityChestDeck.drawCard();
                deck = server.communityChestDeck;
            }
            else if (deckSpace.deck == chanceCommunitySpace.deckType.CHANCE) {
                drawnCard = server.chanceDeck.drawCard();
                deck = server.chanceDeck;
            }
            outcome.cardDrawn.didDrawCard = true;
            outcome.cardDrawn.drawnCard = drawnCard;
            outcome.cardDrawn.deck = deck;
            if (handleDrawnCard(deck, drawnCard))
            {
                deck.moveCardToBottomOfDeck();
            }
        }
        else if (landedSpace instanceof goToJailSpace)
        {
            server.goToJail();
            outcome.gotToJailSpace = true;
        }
        else if (landedSpace instanceof goSpace)
        {
            server.earnGoMoney();
        }
        else if (landedSpace instanceof freeParkingSpace)
        {

        }
        else if (landedSpace instanceof taxSpace)
        {
            int taxAmount = ((taxSpace) landedSpace).taxAmount;
            outcome.rentState = new outcomeResponse().new oweRent();
            if (!canPay(taxAmount))
            {
                initBankruptcy(currentPlayer, null);
            }
            else if (!canPayNow(taxAmount))
            {
                outcome.rentState.needToMortgage = true;
                server.setWaitingPayee(null);
                server.setWaitingPayment(taxAmount);
            }
            else
            {
                currentPlayer.readyCash -= taxAmount;
            }
        }
        else if (landedSpace instanceof jailSpace)
        {

        }

        return this.outcome;
    }

    public void initBankruptcy(Player bankruptPlayer, Player bankrupter)
    {
        if (outcome.rentState == null)
        {
            outcome.rentState = new outcomeResponse().new oweRent();
        }
        outcomeResponse.bankruptState bankrupt = new outcomeResponse().new bankruptState();
        bankrupt.isBankrupt = true;
        bankrupt.bankruptTo = bankrupter;
        bankrupt.bankruptPlayer = bankruptPlayer;
        outcome.rentState.bankrupt.add(bankrupt);
        bankruptPlayer.isBankrupt = true;
        int money = bankruptPlayer.readyCash;
        bankruptPlayer.readyCash = 0;
        if (bankrupter != null)
        {
            bankrupter.readyCash += money;
        }
        for (Property prop : bankruptPlayer.properties)
        {
            prop.buildingLevel = 0;
            if (bankrupter != null)
            {
                bankrupter.properties.add(prop);
            }
            prop.ownedBy = bankrupter;
        }
        bankruptPlayer.properties.removeAll(bankruptPlayer.properties);
        int livePlayers = 0;
        for (Player player : getPlayers())
        {
            if (player.isBankrupt == false)
            {
                livePlayers++;
            }
        }
        if (livePlayers == 1)
        {
            server.gameOver = true;
        }
    }


    public boolean handleDrawnCard(cardQueue deck, Card drawnCard)
    {
        boolean doMoveToBottom = true;

        Player currentPlayer = getCurrentPlayer();
        if (drawnCard instanceof advanceToCard)
        {
            outcome.cardDrawn.continueState = true;
            advanceToCard advanceCard = (advanceToCard) drawnCard;
            int space = advanceCard.advanceToSpace.spacePosition;
            boolean toJail = advanceCard.toJailAdvance;
            if (toJail)
            {
                server.goToJail();
                return doMoveToBottom;
            }
            currentPlayer.previousPosition = currentPlayer.position;
            currentPlayer.position = space;
        }
        else if (drawnCard instanceof collectionCard)
        {
            collectionCard collection = (collectionCard) drawnCard;
            currentPlayer.readyCash += collection.collectionAmount;
        }
        else if (drawnCard instanceof getOutOfJailFreeCard)
        {
            server.addGetOutOfJailFreeCard(drawnCard);
            deck.removeCard();
            doMoveToBottom = false;
        }
        else if (drawnCard instanceof collectionsFromPlayersCard)
        {
            collectionsFromPlayersCard collectionFromPlayers = (collectionsFromPlayersCard) drawnCard;
            int amount = collectionFromPlayers.amountToCollect;
            for (Player player : getPlayers())
            {
                if (player != currentPlayer)
                {
                    if (logic.canHandlePayment(player, amount))
                    {
                        player.readyCash -= amount;
                        currentPlayer.readyCash += amount;
                    }
                    else
                    {
                        initBankruptcy(player, currentPlayer);
                    }
                }
            }
        }
        else if (drawnCard instanceof paymentsToPlayersCard)
        {
            paymentsToPlayersCard paymentToPlayers = (paymentsToPlayersCard) drawnCard;
            int amount = paymentToPlayers.amountToPay;
            int totalAmount = amount * (getPlayers().size() - 1);
            if (!canPay(totalAmount))
            {
                outcome.cardDrawn.bankrupt.isBankrupt = true;
                initBankruptcy(currentPlayer, null);
                return doMoveToBottom;
            }
            if (!canPayNow(totalAmount))
            {
                outcome.cardDrawn.needToMortgage = true;
                server.setWaitingPayee(null);
                server.setWaitingPayment(totalAmount);
            }
            for (Player player : getPlayers())
            {
                if (player != currentPlayer)
                {
                    player.readyCash += amount;
                    if (canPayNow(totalAmount))
                    {
                        currentPlayer.readyCash -= amount;
                    }
                }
            }
        }
        else if (drawnCard instanceof payToBankCard)
        {
            payToBankCard payBankCard = (payToBankCard) drawnCard;
            int amount = payBankCard.paymentAmount;
            if (!canPay(amount))
            {
                outcome.cardDrawn.bankrupt.isBankrupt = true;
                initBankruptcy(currentPlayer, null);
                return doMoveToBottom;
            }
            else if (!canPayNow(amount))
            {
                outcome.cardDrawn.needToMortgage = true;
                server.setWaitingPayee(null);
                server.setWaitingPayment(amount);
            }
            else
            {
                currentPlayer.readyCash -= amount;
            }
        }
        else if (drawnCard instanceof buildingPaymentsCard)
        {
            buildingPaymentsCard buildingPayments = (buildingPaymentsCard) drawnCard;
            int amount = logic.getBuildingRepairsCardPaymentAmount(currentPlayer, buildingPayments.houseRate, buildingPayments.hotelRate);
            if (!canPay(amount))
            {
                outcome.cardDrawn.bankrupt.isBankrupt = true;
                initBankruptcy(currentPlayer, null);
                return doMoveToBottom;
            }
            else if (!canPayNow(amount))
            {
                outcome.cardDrawn.needToMortgage = true;
                server.setWaitingPayee(null);
                server.setWaitingPayment(amount);
            }
            else
            {
                currentPlayer.readyCash -= amount;
            }
        }
        return doMoveToBottom;
    }

    public outcomeResponse.bankruptState payUp()
    {
        int amountToPay = server.getWaitingPayment();
        if (getCurrentPlayer().readyCash < amountToPay)
        {
            outcomeResponse.bankruptState response = new outcomeResponse().new bankruptState();
            response.isBankrupt = true;
            response.bankruptPlayer = getCurrentPlayer();
            response.bankruptTo = server.getWaitingPayee();
            return response;
        }
        getCurrentPlayer().readyCash -= amountToPay;
        if (server.getWaitingPayee() != null)
        {
            server.getWaitingPayee().readyCash += amountToPay;
        }
        server.setWaitingPayment(-1);
        server.setWaitingPayee(null);
        return null;
    }

    public gameStateResponse getCurrentState()
    {
        if (server.getCurrentPlayer().isInJail)
        {
            return new gameStateResponse(true);
        }
        return new gameStateResponse(false);
    }

    public Player getCurrentPlayer()
    {
        return server.getCurrentPlayer();
    }

    public boolean canPay(int amount)
    {
        Player currentPlayer = getCurrentPlayer();
        return logic.canHandlePayment(currentPlayer, amount);
    }

    public boolean canPayNow(int amount)
    {
        return getCurrentPlayer().readyCash >= amount;
    }

    public boolean canPayWaiting()
    {
        int waitingAmount = this.server.getWaitingPayment();
        return (canPayNow(waitingAmount));
    }

    public boolean canPayWaitingAnymore()
    {
        int waitingAmount = this.server.getWaitingPayment();
        return (canPay(waitingAmount));
    }

    public int getBailAmount()
    {
        return server.getBailAmount();
    }

    public boolean isGameOver()
    {
        return server.gameOver;
    }

    public Player theWinner()
    {
        if (server.gameOver)
        {
            for (Player player : getPlayers())
            {
                if (player.isBankrupt == false)
                {
                    return player;
                }
            }
        }
        return null;
    }

    public boolean payBail()
    {
        getCurrentPlayer().readyCash -= getBailAmount();
        getCurrentPlayer().isInJail = false;
        return true;
    }

    public boolean useGetOutOfJailFreeCard()
    {
        if (!canUseGetOutOfJailFreeCard())
        {
            return false;
        }
        Card lastOne = getCurrentPlayer().getOutOfJailFreeCards.get(getCurrentPlayer().getOutOfJailFreeCards.size() - 1);
        if (lastOne.belongingDeck == Card.cardDeckType.CHANCE)
        {
            server.chanceDeck.addCard(lastOne);
        }
        else if (lastOne.belongingDeck == Card.cardDeckType.COMMUNITYCHEST)
        {
            server.communityChestDeck.addCard(lastOne);
        }
        getCurrentPlayer().getOutOfJailFreeCards.remove(lastOne);
        getCurrentPlayer().isInJail = false;
        return true;
    }

    public boolean canUseGetOutOfJailFreeCard()
    {
        if (getCurrentPlayer().getOutOfJailFreeCards.size() > 0)
        {
            return true;
        }
        return false;
    }

    public buyResponse buyProperty(Property property)
    {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.readyCash < property.initialCost && canPay(property.initialCost) == false)
        {
            return new buyResponse(false, false);
        }
        if (currentPlayer.readyCash >= property.initialCost)
        {
            this.server.setWaitingPayment(-1);
            currentPlayer.readyCash -= property.initialCost;
            property.ownedBy = currentPlayer;
            currentPlayer.properties.add(property);
            return new buyResponse(true, false);
        }
        this.server.setWaitingPayment(property.initialCost);
        return new buyResponse(false, true);
    }

    public void doneTurn()
    {
        this.outcome = new outcomeResponse();
        if (server.gameOver)
        {
            return;
        }
        if (!server.onDoubles)
        {
            server.onDoubles = false;
            do
            {
                server.nextPlayerTurn();
            } while (getCurrentPlayer().isBankrupt);
        }
    }

    public boolean canAdjustProperty(Property property)
    {
        Player currentPlayer = getCurrentPlayer();
        if (property.ownedBy == currentPlayer)
        {
            return true;
        }

        return false;
    }

    public boolean canMortgage(Property property)
    {
        if (getCurrentPlayer() != property.ownedBy)
        {
            return false;
        }
        if (property.canMortgage())
        {
            return true;
        }
        return false;
    }

    public boolean canUnMortgage(Property property)
    {
        if (getCurrentPlayer() != property.ownedBy)
        {
            return false;
        }
        if (getCurrentPlayer().readyCash >= property.getUnMortgageCost() && property.canUnMortgage())
        {
            return true;
        }
        return false;
    }

    public boolean mortgageProperty(Property property)
    {
        if (!canMortgage(property))
        {
            return false;
        }
        property.isMortgaged = true;
        property.ownedBy.readyCash += property.getMortgageValue();
        return true;
    }

    public boolean unMortgageProperty(Property property)
    {
        if (!canUnMortgage(property))
        {
            return false;
        }
        property.ownedBy.readyCash -= property.getUnMortgageCost();
        property.isMortgaged = false;
        return true;
    }

    public boolean canRaiseSingle(avenueProperty avenue)
    {
        if (getCurrentPlayer() != avenue.ownedBy)
        {
            return false;
        }
        if (avenue.canRaiseOnSingleProperty(true))
        {
            if (getCurrentPlayer().readyCash >= avenue.buildingCost)
            {
                return true;
            }
        }
        return false;
    }

    public boolean canRaiseSet(avenueProperty avenue)
    {
        if (getCurrentPlayer() != avenue.ownedBy)
        {
            return false;
        }
        if (avenue.canRaiseOnSet())
        {
            int numberOfProperties = avenue.numberOfPropertiesInColorGroup;
            int cost = avenue.buildingCost * numberOfProperties;
            if (getCurrentPlayer().readyCash >= cost)
            {
                return true;
            }
        }
        return false;
    }

    public boolean canTakeDownSingle(avenueProperty avenue)
    {
        if (getCurrentPlayer() != avenue.ownedBy)
        {
            return false;
        }
        return (avenue.canTakeDownOnSingleProperty(true));
    }

    public boolean canTakeDownSet(avenueProperty avenue)
    {
        if (getCurrentPlayer() != avenue.ownedBy)
        {
            return false;
        }
        return (avenue.canTakeDownOnSet());
    }

    public void raiseOnProperty(Property avenue)
    {
        if (canRaiseSingle((avenueProperty) avenue))
        {
            Player currentPlayer = getCurrentPlayer();
            int cost = avenue.buildingCost;
            currentPlayer.readyCash -= cost;
            avenue.buildingLevel += 1;
        }
    }

    public void raiseOnSet(Property avenue)
    {
        if (canRaiseSet((avenueProperty) avenue))
        {
            Player currentPlayer = getCurrentPlayer();
            int cost = avenue.buildingCost * avenue.numberOfPropertiesInColorGroup;
            currentPlayer.readyCash -= cost;
            for (Property ave : avenue.allPropertiesInColorGroup)
            {
                ave.buildingLevel += 1;
            }
        }
    }

    public void takeDownOnProperty(Property avenue)
    {
        if (canTakeDownSingle((avenueProperty) avenue))
        {
            Player currentPlayer = getCurrentPlayer();
            int value = ((avenueProperty) avenue).getTakeDownValue();
            currentPlayer.readyCash += value;
            avenue.buildingLevel -= 1;
        }
    }

    public void takeDownOnSet(Property avenue)
    {
        if (canTakeDownSet((avenueProperty) avenue))
        {
            Player currentPlayer = getCurrentPlayer();
            int value = ((avenueProperty) avenue).getTakeDownValue() * avenue.numberOfPropertiesInColorGroup;
            currentPlayer.readyCash += value;
            for (Property ave : avenue.allPropertiesInColorGroup)
            {
                ave.buildingLevel -= 1;
            }
        }
    }


    public ArrayList<Player> getPossibleDealingPlayers()
    {
        ArrayList<Player> returnPlayers = new ArrayList<>();
        ArrayList<Player> players = getPlayers();
        for (Player player : players)
        {
            if (player != getCurrentPlayer() && player.isBankrupt == false)
            {
                returnPlayers.add(player);
            }
        }
        return returnPlayers;
    }



    public boolean isDealValid(Player dealingPlayer, Player partnerPlayer, String dealingMoney, String partnerMoney, ArrayList<Property> dealingProperties, ArrayList<Property> partnerProperties)
    {
        if (dealingPlayer != getCurrentPlayer())
        {
            return false;
        }
        if (partnerPlayer == null)
        {
            return false;
        }
        if (isNumeric(dealingMoney) == false || isNumeric(partnerMoney) == false)
        {
            return false;
        }
        int dealMoney = Integer.parseInt(dealingMoney);
        int partMoney = Integer.parseInt(partnerMoney);
        if (dealMoney > dealingPlayer.readyCash || partMoney > partnerPlayer.readyCash)
        {
            return false;
        }
        for (Property property : dealingProperties)
        {
            if (property.ownedBy != dealingPlayer || property.partOfMonopoly())
            {
                return false;
            }
        }
        for (Property property : partnerProperties)
        {
            if (property.ownedBy != partnerPlayer || property.partOfMonopoly())
            {
                return false;
            }
        }
        if (dealMoney <= 0 && dealingProperties.size() == 0)
        {
            return false;
        }
        if (partMoney <= 0 && partnerProperties.size() == 0)
        {
            return false;
        }

        return true;
    }

    public void proposeDeal(Player dealingPlayer, Player partnerPlayer, String dealingMoney, String partnerMoney, ArrayList<Property> dealingProperties, ArrayList<Property> partnerProperties)
    {
        if (!isDealValid(dealingPlayer, partnerPlayer, dealingMoney, partnerMoney, dealingProperties, partnerProperties))
        {
            return;
        }
        server.waitingOnDealDecision = partnerPlayer;
    }

    public void acceptDeal(Player dealingPlayer, Player partnerPlayer, String dealingMoney, String partnerMoney, ArrayList<Property> dealingProperties, ArrayList<Property> partnerProperties)
    {
        if (!isDealValid(dealingPlayer, partnerPlayer, dealingMoney, partnerMoney, dealingProperties, partnerProperties))
        {
            return;
        }
        int dealMoney = Integer.parseInt(dealingMoney);
        int partMoney = Integer.parseInt(partnerMoney);
        dealingPlayer.readyCash -= dealMoney;
        dealingPlayer.readyCash += partMoney;
        partnerPlayer.readyCash -= partMoney;
        partnerPlayer.readyCash += dealMoney;
        for (Property prop : dealingProperties)
        {
            prop.ownedBy = partnerPlayer;
            dealingPlayer.properties.remove(prop);
            partnerPlayer.properties.add(prop);
        }
        for (Property prop : partnerProperties)
        {
            prop.ownedBy = dealingPlayer;
            partnerPlayer.properties.remove(prop);
            dealingPlayer.properties.add(prop);
        }
        server.waitingOnDealDecision = null;
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}