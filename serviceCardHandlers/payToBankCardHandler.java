package serviceCardHandlers;

import Cards.Card;
import Cards.cardQueue;
import Cards.payToBankCard;
import app.Player;
import app.monopolyService;
import responses.outcomeResponse;

public class payToBankCardHandler extends basicCardHandler {

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        Player currentPlayer = service.getCurrentPlayer();
        payToBankCard payBankCard = (payToBankCard) drawnCard;
        int amount = payBankCard.paymentAmount;
        if (!service.canPay(amount))
        {
            outcomeIn.cardDrawn.bankrupt.isBankrupt = true;
            outcomeIn = service.initBankruptcy(currentPlayer, null, outcomeIn);
        }
        else if (!service.canPayNow(amount))
        {
            outcomeIn.cardDrawn.needToMortgage = true;
            service.server.setWaitingPayee(null);
            service.server.setWaitingPayment(amount);
        }
        else
        {
            currentPlayer.readyCash -= amount;
        }

        return outcomeIn;
    }

}