package gameSpaces;

public interface PropertyInterface {

    int getRent(int roll);

    boolean canRaiseOnSingleProperty(boolean evenBuild);

    boolean canRaiseOnSet();

    boolean canTakeDownOnSingleProperty(boolean evenBuild);

    boolean canTakeDownOnSet();

    void addPropertyToSet(boardSpace... args);

}