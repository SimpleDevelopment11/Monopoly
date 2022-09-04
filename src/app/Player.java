package app;

import Cards.*;
import gameSpaces.Property;

import java.util.ArrayList;

public class Player {

    public int position;
    public int previousPosition;
    public int readyCash;
    public int playerNumber;
    public ArrayList<Property> properties;
    public boolean isInJail;
    public boolean isBankrupt;
    public int numberOfDoubles;
    public String playerName;
    public ArrayList<Card> getOutOfJailFreeCards = new ArrayList<>();

    public Player(int position, int readyCash, int playerNumber)
    {
        this.position = position;
        this.previousPosition = position;
        this.readyCash = readyCash;
        this.playerNumber = playerNumber;
        properties = new ArrayList<>();
        isInJail = false;
        isBankrupt = false;
        numberOfDoubles = 0;
    }

}