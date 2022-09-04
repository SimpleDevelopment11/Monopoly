package serviceCardHandlers;

import Cards.Card;
import Cards.buildingPaymentsCard;
import Cards.cardQueue;
import app.Player;
import app.monopolyService;
import responses.outcomeResponse;

public class buildingPaymentsCardHandler extends basicCardHandler {

    public outcomeResponse handleCardEvent(monopolyService service, outcomeResponse outcomeIn, Card drawnCard, cardQueue deck) {
        Player currentPlayer = service.getCurrentPlayer();
        buildingPaymentsCard buildingPayments = (buildingPaymentsCard) drawnCard;
        int amount = service.logic.getBuildingRepairsCardPaymentAmount(currentPlayer, buildingPayments.houseRate, buildingPayments.hotelRate);
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