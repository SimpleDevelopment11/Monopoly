package gameSpaces;

public class goSpace extends boardSpace {

    public int bonusLandAmount;

    public goSpace(int bonus, int position, String name)
    {
        super(position, name);
        bonusLandAmount = bonus;
    }

}