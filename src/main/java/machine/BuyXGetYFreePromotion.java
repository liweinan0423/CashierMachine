package machine;

public class BuyXGetYFreePromotion extends Promotion {
    private final int x;
    private final int y;

    public BuyXGetYFreePromotion(int x, int y, String... productCodes) {
        super(productCodes);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void apply(Item item) {
        item.setQuantity(item.getQuantity() + item.getQuantity() / getX() * getY());
        item.setSubTotal(item.getQuantity() * item.getPrice());
    }

    @Override
    public boolean shouldPrintSavingForItem(Item item) {
        return false;
    }

    @Override
    public boolean shouldPrintSavingInSummary(Order order) {
        return true;
    }

    @Override
    public boolean shouldPrintPromotionSummary(Order order) {
        return true;
    }

}
