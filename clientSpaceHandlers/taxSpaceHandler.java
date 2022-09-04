package clientSpaceHandlers;

import app.ClientGame;
import app.guiBoard;
import app.monopolyService;
import gameSpaces.taxSpace;
import responses.outcomeResponse;

public class taxSpaceHandler extends basicHandler {

    private outcomeResponse outcome;

    public taxSpaceHandler(outcomeResponse outcome)
    {
        super();
        this.outcome = outcome;
    }

    public synchronized boolean handleLandedEvent(ClientGame parentGame, monopolyService service) throws InterruptedException {
        guiBoard myGui = parentGame.myGui;
        taxSpace tax = (taxSpace) landedSpace;
        myGui.createMessageDialog("Taxes...", "Cannot avoid taxes I'm afraid.", "You must pay the bank $" + tax.taxAmount + ".");

        if (outcome.rentState != null && outcome.rentState.needToMortgage)
        {
            parentGame.arrangingFinances = true;
            myGui.toggleButtons(false);
            wait();
            outcomeResponse.bankruptState response = service.payUp();
            if (response == null)
            {
                myGui.toggleButtons(true);
            }
            else
            {
                parentGame.goBankrupt(response.bankruptPlayer, response.bankruptTo);
            }
        }
        else if (outcome.rentState != null && outcome.rentState.bankrupt.size() > 0)
        {
            for (outcomeResponse.bankruptState bankruption : outcome.rentState.bankrupt)
            {
                parentGame.goBankrupt(bankruption.bankruptPlayer, bankruption.bankruptTo);
            }
        }

        return true;
    }

}
