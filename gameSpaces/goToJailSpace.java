package gameSpaces;

public class goToJailSpace extends boardSpace {

    public int jailPosition;

    public goToJailSpace(int jailPosition, int position, String name)
    {
        super(position, name);
        this.jailPosition = jailPosition;
    }

}