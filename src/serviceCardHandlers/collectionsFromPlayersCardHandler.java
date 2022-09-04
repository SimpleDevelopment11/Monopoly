package serviceCardHandlers;

import Cards.Card;
import Cards.cardQueue;
import Cards.collectionsFromPlayersCard;
import app.Player;
import app.monopolyService;
import responses.outcomeResponse;

public class collectionsFromPlayersCardHandler extends basicCardHandler {

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        Player currentPlayer = service.getCurrentPlayer();
        collectionsFromPlayersCard collectionFromPlayers = (collectionsFromPlayersCard) drawnCard;
        int amount = collectionFromPlayers.amountToCollect;
        for (Player player : service.getPlayers())
        {
            if (player != currentPlayer)
            {
                if (service.logic.canHandlePayment(player, amount))
                {
                    player.readyCash -= amount;
                    currentPlayer.readyCash += amount;
                }
                else
                {
                    outcomeIn = service.initBankruptcy(player, currentPlayer, outcomeIn);
                }
            }
        }

        return outcomeIn;
    }

}
