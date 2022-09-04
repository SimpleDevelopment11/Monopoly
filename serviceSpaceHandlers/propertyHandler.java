package serviceSpaceHandlers;

import app.Player;
import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;
import gameSpaces.Property;
import responses.outcomeResponse;

public class propertyHandler extends basicHandler{

    private outcomeResponse outcome = new outcomeResponse();

    public void handleLandedEvent(monopolyService service) {
        Player currentPlayer = service.getCurrentPlayer();
        Property propertySpace = (Property) landedSpace;
        if (service.logic.canBuyProperty(propertySpace, service))
        {
            outcome.canBuyProperty = true;
        }
        else if (propertySpace.ownedBy != null && propertySpace.ownedBy != currentPlayer)
        {
            outcome.rentState = new outcomeResponse().new oweRent();
            outcome.rentState.oweOrNot = true;
            int rent = propertySpace.getRent(service.server.currentRoll);
            outcome.rentState.amountOwed = rent;
            Player owner = propertySpace.ownedBy;
            if (currentPlayer.readyCash >= rent)
            {
                currentPlayer.readyCash -= rent;
                owner.readyCash += rent;
            }
            else if(service.canPay(rent))
            {
                service.server.setWaitingPayment(rent);
                service.server.setWaitingPayee(owner);
                outcome.rentState.needToMortgage = true;
            }
            else
            {
                service.initBankruptcy(currentPlayer, owner);
            }
        }
    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.propertyHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.propertyHandler(outcome);
    }
}