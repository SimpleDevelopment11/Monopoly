package serviceCardHandlers;

import Cards.Card;
import Cards.advanceToCard;
import Cards.cardQueue;
import app.Player;
import app.monopolyService;
import responses.outcomeResponse;

public class advanceToCardHandler extends basicCardHandler{

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        Player currentPlayer = service.getCurrentPlayer();
        outcomeIn.cardDrawn.continueState = true;
        advanceToCard advanceCard = (advanceToCard) drawnCard;
        int space = advanceCard.advanceToSpace.spacePosition;
        boolean toJail = advanceCard.toJailAdvance;
        if (toJail)
        {
            service.server.goToJail();
        }
        else
        {
            currentPlayer.previousPosition = currentPlayer.position;
            currentPlayer.position = space;
        }

        return outcomeIn;
    }

}