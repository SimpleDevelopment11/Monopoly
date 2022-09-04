package Cards;

public class payToBankCard extends Card {

    public int paymentAmount;

    public payToBankCard(String message, int amount, cardDeckType deck)
    {
        super(message, deck);
        paymentAmount = amount;
    }

}