package serviceSpaceHandlers;

import app.Player;
import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;
import gameSpaces.Property;

public class propertyHandler extends basicHandler{

    public void handleLandedEvent(monopolyService service) {
        Player currentPlayer = service.getCurrentPlayer();
        Property propertySpace = (Property) landedSpace;
        if (propertySpace.ownedBy == null && service.canPay(propertySpace.initialCost))
        {
            //Can buy property
        }
        else if (propertySpace.ownedBy != null && propertySpace.ownedBy != currentPlayer)
        {
            int rent = propertySpace.getRent(service.server.currentRoll);
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
            }
            else
            {
                service.initBankruptcy(currentPlayer, owner);
            }
        }
    }


    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.propertyHandler();
    }
}