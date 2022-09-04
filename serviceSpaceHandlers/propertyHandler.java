package serviceSpaceHandlers;

import app.Player;
import app.monopolyService;
import gameSpaces.Property;

public class propertyHandler extends basicHandler{

    public void handleLandedEvent(monopolyService monoSerivce) {
        Player currentPlayer = monoSerivce.getCurrentPlayer();
        Property propertySpace = (Property) landedSpace;
        if (propertySpace.ownedBy == null && monoSerivce.canPay(propertySpace.initialCost))
        {
            //Can buy property
        }
        else if (propertySpace.ownedBy != null && propertySpace.ownedBy != currentPlayer)
        {
            int rent = propertySpace.getRent(monoSerivce.server.currentRoll);
            Player owner = propertySpace.ownedBy;
            if (currentPlayer.readyCash >= rent)
            {
                currentPlayer.readyCash -= rent;
                owner.readyCash += rent;
            }
            else if(monoSerivce.canPay(rent))
            {
                monoSerivce.server.setWaitingPayment(rent);
                monoSerivce.server.setWaitingPayee(owner);
            }
            else
            {
                monoSerivce.initBankruptcy(currentPlayer, owner);
            }
        }
    }
}