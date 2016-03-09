package machine.promotion;

import machine.order.Item;

/**
 * This composition of promotion contains 2 promotions. Following rules are applied:
 * 1. If a product is eligible for both of the 2 promotions, the promotion with higher priority will take place while the promotion with low priority will be ignored.
 * 2. If a product is eligible for only 1 out of the 2 promotions, the promotion applies as is.
 * 3. If a product is not eligible for neither of the 2 promotions, no action will be taken.
 */
public class CompositePromotion extends Promotion {

    private final Promotion highPriority;
    private final Promotion lowPriority;

    public CompositePromotion(Promotion highPriority, Promotion lowPriority) {
        this.highPriority = highPriority;
        this.lowPriority = lowPriority;
    }


    @Override
    public boolean supports(Item item) {
        return highPriority.supports(item) || lowPriority.supports(item);
    }

    @Override
    public void apply(Item item) {
        if (highPriority.supports(item)) {
            highPriority.apply(item);
        } else if (lowPriority.supports(item)) {
            highPriority.apply(item);
        }
    }
}
