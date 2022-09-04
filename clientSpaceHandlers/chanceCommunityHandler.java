package clientSpaceHandlers;

import Cards.Card;
import app.ClientGame;
import app.guiBoard;
import app.monopolyService;
import responses.outcomeResponse;

public class chanceCommunityHandler extends basicHandler {

    public Card cardDrawn;
    private outcomeResponse outcome;

    public chanceCommunityHandler(Card cardDrawn, outcomeResponse outcome)
    {
        super();
        this.cardDrawn = cardDrawn;
        this.outcome = outcome;
    }

    public synchronized boolean handleLandedEvent(ClientGame parentGame, monopolyService service) throws InterruptedException {
        guiBoard myGui = parentGame.myGui;
        myGui.showDrawnCard(outcome.cardDrawn.deck.typeOfDeck, outcome.cardDrawn.drawnCard.cardMessage);
        myGui.movePlayerPiece(currentPlayer);
        parentGame.declarePlayerTurn();
        boolean reset = true;
        if (outcome.cardDrawn.bankrupt.isBankrupt)
        {
            reset = false;
        }
        else if (outcome.cardDrawn.needToMortgage)
        {
            reset = false;
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
        if (reset && outcome.cardDrawn.continueState)
        {
            parentGame.startThread();
            return false;
        }
        return true;
    }

}