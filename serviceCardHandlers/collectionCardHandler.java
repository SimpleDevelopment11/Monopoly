package serviceCardHandlers;

import Cards.Card;
import Cards.cardQueue;
import Cards.collectionCard;
import app.Player;
import app.monopolyService;
import responses.outcomeResponse;

public class collectionCardHandler extends basicCardHandler {

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        Player currentPlayer = service.getCurrentPlayer();
        collectionCard collection = (collectionCard) drawnCard;
        currentPlayer.readyCash += collection.collectionAmount;

        return outcomeIn;
    }

}