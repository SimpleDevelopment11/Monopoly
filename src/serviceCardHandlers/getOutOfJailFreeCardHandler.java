package serviceCardHandlers;

import Cards.Card;
import Cards.cardQueue;
import app.monopolyService;
import responses.outcomeResponse;

public class getOutOfJailFreeCardHandler extends basicCardHandler {

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        service.server.addGetOutOfJailFreeCard(drawnCard);
        deck.removeCard();
        outcomeIn.cardDrawn.moveCardToBottom = false;

        return outcomeIn;
    }

}