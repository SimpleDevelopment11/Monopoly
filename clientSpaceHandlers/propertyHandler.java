package clientSpaceHandlers;

import app.ClientGame;
import app.guiBoard;
import app.monopolyService;
import gameSpaces.Property;
import responses.buyResponse;
import responses.outcomeResponse;

public class propertyHandler extends basicHandler {

    private outcomeResponse outcome;

    public propertyHandler(outcomeResponse outcome)
    {
        super();
        this.outcome = outcome;
    }

    public synchronized boolean handleLandedEvent(ClientGame parentGame, monopolyService service) throws InterruptedException {
        parentGame.declarePlayerTurn();
        guiBoard myGui = parentGame.myGui;
        if (outcome.canBuyProperty) {
            Property property = (Property) landedSpace;
            if (myGui.askToBuy(property)) {
                buyResponse buyOption = service.buyProperty(property);
                if (buyOption == null)
                {
                    return true;
                }
                if (buyOption.didBuy) {
                    myGui.changeOwnership(property);
                } else if (buyOption.meedToMortgage) {
                    parentGame.arrangingFinances = true;
                    myGui.toggleButtons(false);
                    wait();
                    buyOption = service.buyProperty(property);
                    if (buyOption.didBuy) {
                        myGui.changeOwnership(property);
                    }
                    myGui.toggleButtons(true);
                }
            }
        } else if (outcome.rentState != null && outcome.rentState.oweOrNot == true) {
            myGui.showRentExchange((Property) landedSpace, currentPlayer, ((Property) landedSpace).ownedBy, outcome.rentState.amountOwed);
            if (outcome.rentState.needToMortgage) {
                parentGame.arrangingFinances = true;
                myGui.toggleButtons(false);
                wait();
                outcomeResponse.bankruptState response = service.payUp();
                if (response == null) {
                    myGui.toggleButtons(true);
                } else {
                    parentGame.goBankrupt(response.bankruptPlayer, response.bankruptTo);
                }
            }
            else if (outcome.rentState.bankrupt.size() > 0) {
                for (outcomeResponse.bankruptState bankruption : outcome.rentState.bankrupt) {
                    parentGame.goBankrupt(bankruption.bankruptPlayer, bankruption.bankruptTo);
                }
            }
        }

        return true;
    }
}