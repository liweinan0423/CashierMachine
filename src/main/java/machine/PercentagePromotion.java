package machine;

public class PercentagePromotion extends Promotion {
    private final double percentage;

    public PercentagePromotion(double percentage, String... barcodes) {
        super(barcodes);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public void apply(Item item) {
        item.setTotalPayable(item.getSubTotal() * getPercentage());
    }

    @Override
    public boolean shouldPrintSavingForItem(Item item) {
        return true;
    }

    @Override
    public boolean shouldPrintSavingInSummary(Order order) {
        return true;
    }

    @Override
    public boolean shouldPrintPromotionSummary(Order order) {
        return false;
    }

    boolean supports(Item item) {
        return getBarcodes().contains(item.getProductCode());
    }
}
