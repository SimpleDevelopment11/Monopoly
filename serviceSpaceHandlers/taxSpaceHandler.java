package serviceSpaceHandlers;

import app.Player;
import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;
import gameSpaces.taxSpace;
import responses.outcomeResponse;

public class taxSpaceHandler extends basicHandler{

    private outcomeResponse outcome = new outcomeResponse();

    public void handleLandedEvent(monopolyService service)
    {
        Player currentPlayer = service.getCurrentPlayer();
        int taxAmount = ((taxSpace) landedSpace).taxAmount;
        outcome.rentState = new outcomeResponse().new oweRent();
        if (!service.canPay(taxAmount))
        {
            service.initBankruptcy(currentPlayer, null);
        }
        else if (!service.canPayNow(taxAmount))
        {
            outcome.rentState.needToMortgage = true;
            service.server.setWaitingPayee(null);
            service.server.setWaitingPayment(taxAmount);
        }
        else
        {
            currentPlayer.readyCash -= taxAmount;
        }
    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.taxSpaceHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.taxSpaceHandler(outcome);
    }

}
