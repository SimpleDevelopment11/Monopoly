package app;

import clientSpaceHandlers.basicHandler;
import clientSpaceHandlers.clientSpaceHandler;
import gameSpaces.*;
import responses.buyResponse;
import responses.gameStateResponse;
import responses.outcomeResponse;
import responses.rollResponse;

import java.util.ArrayList;

public class ClientGame {

    public Player[] players;
    public guiBoard myGui;
    private monopolyService service;
    private Board gameBoard;
    public boolean arrangingFinances = false;
    private Thread evaluateRollThread;

    public static void main(String[] args) {
        ClientGame clientGame = new ClientGame();
        clientGame.service = new monopolyService();
        clientGame.myGui = new guiBoard(clientGame);
        clientGame.myGui.initializeSpaces();
        clientGame.players = new Player[4];
        for (int counter = 0; counter < clientGame.players.length; counter++)
        {
            clientGame.players[counter] = clientGame.service.addPlayer();
        }
        ArrayList<Player> players = clientGame.service.getPlayers();
        for (Player player : players)
        {
            clientGame.myGui.addPlayerPiece(player);
        }
        clientGame.declarePlayerTurn();
    }

    public Board getBoard()
    {
        this.gameBoard = this.service.getBoard();
        return this.gameBoard;
    }

    public void roll() throws InterruptedException {
        Player currentPlayer = this.service.getCurrentPlayer();
        rollResponse response = service.handleRoll(players);
        int[] roll = response.roll;
        String message = response.message;
        this.myGui.CONTROLPANEL.updateGameMessage("You rolled a: " + roll[0] + " and a " + roll[1]);
        this.myGui.movePlayerPiece(currentPlayer);
        this.myGui.CONTROLPANEL.updateServerMessage(message);
        startThread();
    }

    public void startThread()
    {
        clientSpaceHandler clientHandler = service.determineRollOutcome();
        this.evaluateRollThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    afterRollEvaluate(clientHandler);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        evaluateRollThread.start();
    }


    private synchronized void afterRollEvaluate(clientSpaceHandler clientHandler) throws InterruptedException {
        Player currentPlayer = this.service.getCurrentPlayer();

        if (clientHandler != null)
        {
            ((basicHandler) clientHandler).currentPlayer = currentPlayer;
            clientHandler.handleLandedEvent(this, this.service);
        }

        /*if (outcome.turnOver == false)
        {
            declarePlayerTurn();
            if (outcome.canBuyProperty)
            {
                Property property = (Property) outcome.landedSpace;
                if (myGui.askToBuy(property))
                {
                    buyResponse buyOption = service.buyProperty(property);
                    if (buyOption.didBuy)
                    {
                        myGui.changeOwnership(property);
                    }
                    else if (buyOption.meedToMortgage)
                    {
                        arrangingFinances = true;
                        myGui.toggleButtons(false);
                        wait();
                        buyOption = service.buyProperty(property);
                        if (buyOption.didBuy)
                        {
                            myGui.changeOwnership(property);
                        }
                        myGui.toggleButtons(true);
                    }
                }
            }
            else if (outcome.rentState != null && outcome.rentState.oweOrNot == true)
            {
                myGui.showRentExchange((Property) outcome.landedSpace, currentPlayer, ((Property) outcome.landedSpace).ownedBy, outcome.rentState.amountOwed);
                if (outcome.rentState.needToMortgage)
                {
                    arrangingFinances = true;
                    myGui.toggleButtons(false);
                    wait();
                    outcomeResponse.bankruptState response = service.payUp();
                    if (response == null)
                    {
                        myGui.toggleButtons(true);
                    }
                    else
                    {
                        goBankrupt(response.bankruptPlayer, response.bankruptTo);
                    }
                }
                else if (outcome.rentState.bankrupt.size() > 0)
                {
                    for (outcomeResponse.bankruptState bankruption : outcome.rentState.bankrupt)
                    {
                        goBankrupt(bankruption.bankruptPlayer, bankruption.bankruptTo);
                    }
                }
            }
            else if (outcome.cardDrawn.didDrawCard)
            {
                myGui.showDrawnCard(outcome.cardDrawn.deck.typeOfDeck, outcome.cardDrawn.drawnCard.cardMessage);
                myGui.movePlayerPiece(currentPlayer);
                declarePlayerTurn();
                boolean reset = true;
                if (outcome.cardDrawn.bankrupt.isBankrupt)
                {
                    reset = false;
                }
                else if (outcome.cardDrawn.needToMortgage)
                {
                    reset = false;
                    arrangingFinances = true;
                    myGui.toggleButtons(false);
                    wait();
                    outcomeResponse.bankruptState response = service.payUp();
                    if (response == null)
                    {
                        myGui.toggleButtons(true);
                    }
                    else
                    {
                        goBankrupt(response.bankruptPlayer, response.bankruptTo);
                    }
                }
                if (reset && outcome.cardDrawn.continueState)
                {
                    startThread();
                    return;
                }
            }
            else if (outcome.gotToJailSpace)
            {
                myGui.createMessageDialog("Tough One", "Time to do Time...", "You landed on go to jail, so that is where you shall go.");
                this.myGui.movePlayerPiece(currentPlayer);
            }
            else if (outcome.rentState != null && outcome.rentState.needToMortgage)
            {
                arrangingFinances = true;
                myGui.toggleButtons(false);
                wait();
                outcomeResponse.bankruptState response = service.payUp();
                if (response == null)
                {
                    myGui.toggleButtons(true);
                }
                else
                {
                    goBankrupt(response.bankruptPlayer, response.bankruptTo);
                }
            }
        }*/

        /* On to next player */
        service.doneTurn();
        declarePlayerTurn();
        gameStateResponse gameState = service.getCurrentState();
        if (gameState.jailTime)
        {
            this.myGui.toggleButtons(false);
            this.myGui.guiJailAsker(true);
        }
        else if (service.isGameOver())
        {
            this.myGui.toggleButtons(false);
        }
        else
        {
            this.myGui.toggleButtons(true);
        }
    }

    public void goBankrupt(Player bankruptPlayer, Player bankrupter)
    {
        myGui.showBankruptcy(bankruptPlayer, bankrupter);
        if (bankrupter != null)
        {
            for (Property prop : bankrupter.properties)
            {
                if (prop instanceof avenueProperty)
                {
                    myGui.removeAllBuildingsGui((avenueProperty) prop);
                    for (int x = 0; x < prop.buildingLevel; x++)
                    {
                        myGui.raiseGui((avenueProperty) prop);
                    }
                }
                myGui.changeOwnership(prop);
            }
        }
        if (service.isGameOver())
        {
            myGui.toggleButtons(false);
            Player winningPlayer = service.theWinner();
            myGui.showEndGame(winningPlayer);
        }
    }

    public synchronized void arrangeFinances()
    {
        if (this.service.canPayWaiting() || !this.service.canPayWaitingAnymore())
        {
            myGui.adjustPropertiesPane.dispose();
            notify();
        }
    }

    public int getBailAmount()
    {
        return service.getBailAmount();
    }

    public void payBail()
    {
        if (service.payBail())
        {
            this.myGui.toggleButtons(true);
        }
        declarePlayerTurn();
    }

    public void useGetOutOfJailFreeCard()
    {
        if (service.useGetOutOfJailFreeCard())
        {
            this.myGui.toggleButtons(true);
        }
        declarePlayerTurn();
    }

    public boolean canPay(int amount)
    {
        return service.canPay(amount);
    }

    public boolean canPayNow(int amount)
    {
        return service.canPayNow(amount);
    }

    public void declarePlayerTurn()
    {
        Player currentPlayer = this.service.getCurrentPlayer();
        this.myGui.CONTROLPANEL.updateTurnLabel("It is Player " + currentPlayer.playerNumber + "'s Turn!");
        myGui.PLAYERINFOPANEL.changeMoneyAmount(service.getCurrentPlayer().readyCash);
    }

    public void mortgageProperty(Property property)
    {
        service.mortgageProperty(property);
        if (this.arrangingFinances)
        {
            this.arrangeFinances();
        }
        declarePlayerTurn();
    }

    public void unMortgageProperty(Property property)
    {
        service.unMortgageProperty(property);
        declarePlayerTurn();
    }

    public void raiseOnProperty(Property property)
    {
        service.raiseOnProperty(property);
        myGui.raiseGui((avenueProperty) property);
        declarePlayerTurn();
    }

    public void raiseOnSet(Property property)
    {
        service.raiseOnSet(property);
        for (Property avenue : ((avenueProperty) property).allPropertiesInColorGroup) {
            myGui.raiseGui((avenueProperty) avenue);
        }
        declarePlayerTurn();
    }

    public void takeDownOnProperty(Property property)
    {
        service.takeDownOnProperty(property);
        if (this.arrangingFinances)
        {
            this.arrangeFinances();
        }
        myGui.takeDownGui((avenueProperty) property);
        declarePlayerTurn();
    }

    public void takeDownSet(Property property)
    {
        service.takeDownOnSet(property);
        if (this.arrangingFinances)
        {
            this.arrangeFinances();
        }
        for (Property avenue : ((avenueProperty) property).allPropertiesInColorGroup) {
            myGui.takeDownGui((avenueProperty) avenue);
        }
        declarePlayerTurn();
    }


    public boolean canAdjustProperty(Property property)
    {
        return service.canAdjustProperty(property);
    }

    public boolean canMortgage(Property property)
    {
        return service.canMortgage(property);
    }

    public boolean canUnMortgage(Property property)
    {
        return service.canUnMortgage(property);
    }

    public boolean canRaiseSingle(avenueProperty avenue)
    {
        return service.canRaiseSingle(avenue);
    }

    public boolean canRaiseSet(avenueProperty avenue)
    {
        return service.canRaiseSet(avenue);
    }

    public boolean canTakeDownSingle(avenueProperty avenue)
    {
        return service.canTakeDownSingle(avenue);
    }

    public boolean canTakeDownSet(avenueProperty avenue)
    {
        return service.canTakeDownSet(avenue);
    }

    public boolean canUseGetOutOfJailFreeCard()
    {
        return service.canUseGetOutOfJailFreeCard();
    }

    public Player getDealingPlayer()
    {
        return service.getCurrentPlayer();
    }

    public ArrayList<Player> getPossibleDealingPlayers()
    {
        return service.getPossibleDealingPlayers();
    }

    public boolean isDealValid(Player dealingPlayer, Player partnerPlayer, String dealingMoney, String partnerMoney, ArrayList<Property> dealingProperties, ArrayList<Property> partnerProperties)
    {
        return service.isDealValid(dealingPlayer, partnerPlayer, dealingMoney, partnerMoney, dealingProperties, partnerProperties);
    }

    public void proposeDeal(Player dealingPlayer, Player partnerPlayer, String dealingMoney, String partnerMoney, ArrayList<Property> dealingProperties, ArrayList<Property> partnerProperties)
    {
        service.proposeDeal(dealingPlayer, partnerPlayer, dealingMoney, partnerMoney, dealingProperties, partnerProperties);
        if (inArray(players, partnerPlayer))
        {
            myGui.dealPanel.enableAcceptance();
        }
    }

    public void acceptDeal(Player dealingPlayer, Player partnerPlayer, String dealingMoney, String partnerMoney, ArrayList<Property> dealingProperties, ArrayList<Property> partnerProperties)
    {
        service.acceptDeal(dealingPlayer, partnerPlayer, dealingMoney, partnerMoney, dealingProperties, partnerProperties);
        declarePlayerTurn();
        for (Property prop : dealingProperties)
        {
            myGui.changeOwnership(prop);
        }
        for (Property prop : partnerProperties)
        {
            myGui.changeOwnership(prop);
        }
    }

    public boolean inArray(Object[] arr, Object element)
    {
        for (Object object : arr)
        {
            if (object == element)
            {
                return true;
            }
        }
        return false;
    }

}