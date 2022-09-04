package serviceSpaceHandlers;

import Cards.Card;
import Cards.cardQueue;
import app.monopolyService;
import clientSpaceHandlers.clientSpaceHandler;
import gameSpaces.chanceCommunitySpace;
import responses.outcomeResponse;

public class chanceCommunityHandler extends basicHandler{

    private outcomeResponse outcome = new outcomeResponse();

    private Card drawnCard = null;

    public void handleLandedEvent(monopolyService service)
    {
        chanceCommunitySpace deckSpace = (chanceCommunitySpace) landedSpace;

        Card drawnCard = null;
        cardQueue deck = null;
        if (deckSpace.deck == chanceCommunitySpace.deckType.COMMUNITYCHEST) {
            drawnCard = service.server.communityChestDeck.drawCard();
            deck = service.server.communityChestDeck;
        }
        else if (deckSpace.deck == chanceCommunitySpace.deckType.CHANCE) {
            drawnCard = service.server.chanceDeck.drawCard();
            deck = service.server.chanceDeck;
        }
        outcome.cardDrawn.didDrawCard = true;
        outcome.cardDrawn.drawnCard = drawnCard;
        outcome.cardDrawn.deck = deck;
        outcome = drawnCard.handler.handleCardEvent(service, outcome, drawnCard, deck);
        if (outcome.cardDrawn.moveCardToBottom)
        {
            deck.moveCardToBottomOfDeck();
        }
    }

    public serviceSpaceHandler getNewHandler()
    {
        return new serviceSpaceHandlers.chanceCommunityHandler();
    }

    public clientSpaceHandler getClientHandler() {
        return new clientSpaceHandlers.chanceCommunityHandler(drawnCard, outcome);
    }

}