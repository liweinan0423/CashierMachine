package machine;

public class NOPPromotion extends Promotion {
    public NOPPromotion(String... barcodes) {
        super(barcodes);
    }

    @Override
    public void apply(Item item) {
    }

    @Override
    public boolean shouldPrintSavingForItem(Item item) {
        return false;
    }

    @Override
    public boolean shouldPrintSavingInSummary(Order order) {
        return false;
    }

    @Override
    public boolean shouldPrintPromotionSummary(Order order) {
        return false;
    }
}
