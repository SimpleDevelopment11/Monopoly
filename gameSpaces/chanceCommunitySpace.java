package gameSpaces;

import serviceSpaceHandlers.chanceCommunityHandler;

public class chanceCommunitySpace extends boardSpace {

    public deckType deck;

    public chanceCommunitySpace(int position, String name, deckType type)
    {
        super(position, name, new chanceCommunityHandler());
        deck = type;
    }

    public enum deckType
    {
        COMMUNITYCHEST,
        CHANCE;
    }

}
