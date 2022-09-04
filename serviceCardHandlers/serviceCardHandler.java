package serviceCardHandlers;

import Cards.Card;
import Cards.cardQueue;
import app.monopolyService;
import responses.outcomeResponse;

public interface serviceCardHandler {

    outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck);

}