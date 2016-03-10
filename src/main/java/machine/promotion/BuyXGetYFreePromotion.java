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

    @Override
    public boolean supports(Item item) {
        return super.supports(item) && item.getQuantity() >= x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void apply(Item item) {
        final int freeQuantity = freeQuantity(item);
        item.increaseBy(freeQuantity);
        item.setSaving(freeQuantity * item.getProduct().getPrice());
        item.setPromotion(this);
    }

    private int freeQuantity(Item item) {
        return item.getQuantity() / getX() * getY();
    }
}
