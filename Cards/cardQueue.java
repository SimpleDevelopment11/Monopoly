package Cards;

import gameSpaces.boardSpace;

import java.util.ArrayList;
import java.util.Random;

public class cardQueue {

    private ArrayList<Card> cards;
    private int numCards;
    //deckLocation is an index for the current position in the deck
    private int deckLocation;
    public Card.cardDeckType typeOfDeck;

    public cardQueue(Card.cardDeckType deckType)
    {
        typeOfDeck = deckType;
        deckLocation = 0;
    }

    public void shuffleCards(ArrayList<Card> cardsToShuffle)
    {
        ArrayList<Card> deck = cardsToShuffle;
        Random rand = new Random();

        for (int i = 0; i< deck.size(); i++) {
            int randomPosition = rand.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(randomPosition));
            deck.set(randomPosition, temp);
        }
    }

    public Card drawCard()
    {
        return cards.get(deckLocation);
    }

    public void moveCardToBottomOfDeck()
    {
        Card topCard = cards.get(deckLocation);
        removeCard();
        addCard(topCard);
    }

    public boolean removeCard()
    {
        if (numCards > 0)
        {
            cards.remove(deckLocation);
            deckLocation = ((deckLocation + 1) % cards.size());
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addCard(Card cardToAdd)
    {
            cards.add(((deckLocation - 1 + cards.size()) % cards.size()), cardToAdd);
            return true;
    }


    public void initCommunityChest(boardSpace jailSpace, boardSpace GoSpace)
    {
        ArrayList<Card> communityChestDeck = new ArrayList<>(16);

        communityChestDeck.add(0, new collectionCard("Bank Error In Your Favor - Collect $200", 200, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(1, new collectionCard("From Sale of Stock, You Get $45", 45, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(2, new collectionCard("You Inherit $100", 100, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(3, new collectionCard("Income Tax Refund - Collect $20", 20, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(4, new collectionCard("Life Insurance Matures - Collect $100", 100, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(5, new collectionCard("Receive For Services $25", 25, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(6, new collectionCard("XMAS Fund Matures - Collect $100", 100, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(7, new collectionCard("You Have Won Second Prize In A Beauty Contest Collect $10", 10, Card.cardDeckType.COMMUNITYCHEST));

        communityChestDeck.add(8, new payToBankCard("Pay School Tax of $150", 150, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(9, new payToBankCard("Pay Hospital $100", 100, Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(10, new payToBankCard("Doctor's Fee Pay $50", 50, Card.cardDeckType.COMMUNITYCHEST));

        communityChestDeck.add(11, new advanceToCard(jailSpace, true,"Go To Jail. Go Directly to Jail. DO NOT PASS GO. DO NOT COLLECT $200", Card.cardDeckType.COMMUNITYCHEST));
        communityChestDeck.add(12, new advanceToCard(GoSpace, false,"Advance To Go (Collect $200)", Card.cardDeckType.COMMUNITYCHEST));

        communityChestDeck.add(13, new buildingPaymentsCard(40, 115, "You Are Assessed For Street Repairs. $40 Per House; $115 per Hotel", Card.cardDeckType.COMMUNITYCHEST));

        communityChestDeck.add(14, new collectionsFromPlayersCard(50, "Grand Opera Opening. Collect $50 From Every mainCode.Player for opening night seats.", Card.cardDeckType.COMMUNITYCHEST));

        communityChestDeck.add(15, new getOutOfJailFreeCard("Get Out Of Jail, Free. This card may be kept until needed or sold.", Card.cardDeckType.COMMUNITYCHEST));

        shuffleCards(communityChestDeck);
        this.cards = communityChestDeck;
        numCards = this.cards.size();
    }

    public void initChance(boardSpace stCharles, boardSpace readingRailroad, boardSpace GO, boardSpace JAIL, boardSpace illinoisAvenue, boardSpace boardWalk)
    {
        ArrayList<Card> chanceDeck = new ArrayList<Card>(12);

        chanceDeck.add(0, new advanceToCard(stCharles, false,"Advance to St. Charles Place", Card.cardDeckType.CHANCE));
        chanceDeck.add(1, new advanceToCard(readingRailroad, false,"Take a Ride on the Reading", Card.cardDeckType.CHANCE));
        chanceDeck.add(2, new advanceToCard(GO, false,"Advance TO GO", Card.cardDeckType.CHANCE));
        chanceDeck.add(3, new advanceToCard(JAIL, true,"Go Directly to Jail", Card.cardDeckType.CHANCE));
        chanceDeck.add(4, new advanceToCard(illinoisAvenue, false,"Advance to Illinois Ave.", Card.cardDeckType.CHANCE));
        chanceDeck.add(5, new advanceToCard(boardWalk, false,"Take a Walk On the mainCode.Board Walk", Card.cardDeckType.CHANCE));

        chanceDeck.add(6, new getOutOfJailFreeCard("Get Out of Jail Free Card.", Card.cardDeckType.CHANCE));

        chanceDeck.add(7, new buildingPaymentsCard(25, 100, "Make General Repairs On All Your Property. For Each House Pay $25. For Each Hotel Pay $100.", Card.cardDeckType.CHANCE));

        chanceDeck.add(8, new collectionCard("Bank Pays You Dividend of $50", 50, Card.cardDeckType.CHANCE));
        chanceDeck.add(9, new collectionCard("Your Building And Loan Matures - Collect $150", 150, Card.cardDeckType.CHANCE));

        chanceDeck.add(10, new payToBankCard("Pay Poor Tax of $15", 15, Card.cardDeckType.CHANCE));

        chanceDeck.add(11, new paymentsToPlayersCard(50,"You Have Been Elected Chairman of the mainCode.Board. Pay Each mainCode.Player $50.", Card.cardDeckType.CHANCE));

        shuffleCards(chanceDeck);
        this.cards = chanceDeck;
        numCards = this.cards.size();
    }

}