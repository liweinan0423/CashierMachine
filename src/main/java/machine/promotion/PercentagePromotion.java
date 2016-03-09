package machine.promotion;

import machine.order.Item;

import java.util.List;

public class PercentagePromotion extends Promotion {
    private final double percentage;

    public PercentagePromotion(double percentage, String... barcodes) {
        super(barcodes);
        this.percentage = percentage;
    }
    public PercentagePromotion(double percentage, List<String> productCodes) {
        super(productCodes);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public void apply(Item item) {
        item.setTotalPayable(item.getSubTotal() * getPercentage());
        item.setPromotion(this);
    }

    public boolean supports(Item item) {
        return getProductCodes().contains(item.getProductCode());
    }
}
