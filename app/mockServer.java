package app;

import Cards.Card;
import Cards.cardQueue;
import gameSpaces.*;

import java.util.ArrayList;

public class mockServer {

    private Board gameBoard;
    private ArrayList<Player> players;
    private final static int startPosition = 0;
    private final static int startMoney = 1500;
    private static int bailAmount = 50;
    private static int doublesJail = 3;
    private int currentPlayerNum;
    private int waitingPayment = -1;
    private Player waitingPayee = null;
    private static int goAmount = 200;
    public boolean onDoubles;
    public int currentRoll;
    public cardQueue communityChestDeck;
    public cardQueue chanceDeck;
    public boolean gameOver = false;
    public Player waitingOnDealDecision = null;


    String[] playerNames = {"Battleship", "Car", "Dog", "Hat"};

    public mockServer()
    {
        this.gameBoard = new Board();
        this.players = new ArrayList<Player>();
        this.currentPlayerNum = 1;
        communityChestDeck = new cardQueue(Card.cardDeckType.COMMUNITYCHEST);
        communityChestDeck.initCommunityChest(gameBoard.JAIL, gameBoard.GO);
        chanceDeck = new cardQueue(Card.cardDeckType.CHANCE);
        chanceDeck.initChance(gameBoard.STCHARLESPLACE, gameBoard.READINGRAILROAD, gameBoard.GO, gameBoard.JAIL, gameBoard.ILLINOISAVE, gameBoard.BOARDWALK);
    }

    public void setWaitingPayment(int amount)
    {
        waitingPayment = amount;
    }

    public int getWaitingPayment()
    {
        return waitingPayment;
    }

    public void setWaitingPayee(Player player)
    {
        waitingPayee = player;
    }

    public Player getWaitingPayee()
    {
        return waitingPayee;
    }

    public int getBailAmount()
    {
        return bailAmount;
    }

    public int doublesCount()
    {
        return doublesJail;
    }

    public Board requestBoard()
    {
        return this.gameBoard;
    }

    public ArrayList<Player> getPlayers()
    {
        return this.players;
    }

    public Player addPlayer()
    {
        int playerNum = this.players.size() + 1;
        Player newPlayer = new Player(startPosition, startMoney, playerNum);
        newPlayer.playerName = playerNames[playerNum - 1];
        this.players.add(newPlayer);
        return newPlayer;
    }

    public int currentPlayerNumber()
    {
        return this.currentPlayerNum;
    }

    public void roll(int combinedRoll)
    {
        Player player = getCurrentPlayer();
        player.previousPosition = player.position;
        player.position = (player.position + combinedRoll) % gameBoard.boardSpaces.length;
    }

    public void earnGoMoney()
    {
        this.getCurrentPlayer().readyCash += goAmount;
    }

    public void increaseDoubles()
    {
        getCurrentPlayer().numberOfDoubles += 1;
    }

    public void resetDoubles()
    {
        getCurrentPlayer().numberOfDoubles = 0;
    }

    public void goToJail()
    {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.isInJail = true;
        currentPlayer.previousPosition = currentPlayer.position;
        currentPlayer.position = gameBoard.JAIL.spacePosition;
        onDoubles = false;
    }

    public int getDoublesCount()
    {
        return getCurrentPlayer().numberOfDoubles;
    }

    public void nextPlayerTurn()
    {
        if (waitingPayment != -1)
        {
            return;
        }
        currentPlayerNum = ((currentPlayerNum) % players.size()) + 1;
        waitingOnDealDecision = null;
    }

    public Player getCurrentPlayer()
    {
        for (Player player : this.players)
        {
            if (player.playerNumber == currentPlayerNum)
            {
                return player;
            }
        }
        return  null;
    }

    public boardSpace getLandedSpace()
    {
        return gameBoard.boardSpaces[getCurrentPlayer().position];
    }

    public void addGetOutOfJailFreeCard(Card getOutFreeCard)
    {
        getCurrentPlayer().getOutOfJailFreeCards.add(getOutFreeCard);
    }

}