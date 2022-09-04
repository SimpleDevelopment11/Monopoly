package gameSpaces;

public class chanceCommunitySpace extends boardSpace {

    public deckType deck;

    public chanceCommunitySpace(int position, String name, deckType type)
    {
        super(position, name);
        deck = type;
    }

    public enum deckType
    {
        COMMUNITYCHEST,
        CHANCE;
    }

}
