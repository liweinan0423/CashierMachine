package machine.promotion;

import machine.order.Item;

import java.util.List;

public class BuyXGetYFreePromotion extends Promotion {
    private final int x;
    private final int y;

    public BuyXGetYFreePromotion(Integer x, Integer y, List<String> productCodes) {
        super(productCodes);
        this.x = x;
        this.y = y;
    }

    private int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void apply(Item item) {
        item.setQuantity(item.getQuantity() + item.getQuantity() / getX() * getY());
        item.setSubTotal(item.getQuantity() * item.getPrice());
        item.setPromotion(this);
    }
}
