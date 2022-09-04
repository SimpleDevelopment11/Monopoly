package responses;

import Cards.Card;
import Cards.cardQueue;
import app.Player;
import gameSpaces.*;

import java.util.ArrayList;

public class outcomeResponse {

    public boolean turnOver = false;
    public boardSpace landedSpace = null;
    public boolean canBuyProperty = false;
    public oweRent rentState = null;
    public drawnCard cardDrawn = new drawnCard();
    public boolean gotToJailSpace = false;


    public class oweRent
    {
        public boolean oweOrNot;
        public int amountOwed;
        public boolean needToMortgage = false;
        public ArrayList<bankruptState> bankrupt = new ArrayList<>();
    }

    public class bankruptState
    {
        public boolean isBankrupt = false;
        public Player bankruptPlayer = null;
        public Player bankruptTo = null;
    }

    public class drawnCard
    {
        public boolean didDrawCard = false;
        public Card drawnCard = null;
        public cardQueue deck = null;
        public boolean needToMortgage = false;
        public bankruptState bankrupt = new bankruptState();
        public boolean continueState = false;
    }

}