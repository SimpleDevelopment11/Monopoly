package serviceCardHandlers;

import Cards.Card;
import Cards.cardQueue;
import Cards.paymentsToPlayersCard;
import app.Player;
import app.monopolyService;
import responses.outcomeResponse;

public class paymentsToPlayersCardHandler extends basicCardHandler{

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        Player currentPlayer = service.getCurrentPlayer();
        paymentsToPlayersCard paymentToPlayers = (paymentsToPlayersCard) drawnCard;
        int amount = paymentToPlayers.amountToPay;
        int totalAmount = amount * (service.getPlayers().size() - 1);
        if (!service.canPay(totalAmount))
        {
            outcomeIn.cardDrawn.bankrupt.isBankrupt = true;
            outcomeIn = service.initBankruptcy(currentPlayer, null, outcomeIn);
        }
        if (!service.canPayNow(totalAmount))
        {
            outcomeIn.cardDrawn.needToMortgage = true;
            service.server.setWaitingPayee(null);
            service.server.setWaitingPayment(totalAmount);
        }
        for (Player player : service.getPlayers())
        {
            if (player != currentPlayer)
            {
                player.readyCash += amount;
                if (service.canPayNow(totalAmount))
                {
                    currentPlayer.readyCash -= amount;
                }
            }
        }

        return outcomeIn;
    }

}
