package gameSpaces;

public class taxSpace extends boardSpace {

    public int taxAmount;

    public taxSpace(int amount, int position, String name)
    {
        super(position, name);
        taxAmount = amount;
    }

}