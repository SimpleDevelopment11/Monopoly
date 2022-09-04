package responses;

public class buyResponse {

    public boolean didBuy;
    public boolean meedToMortgage;

    public buyResponse(boolean didBuy, boolean needToMortgage)
    {
        this.didBuy = didBuy;
        this.meedToMortgage = needToMortgage;
    }

}
