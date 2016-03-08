package machine;

import java.util.Arrays;
import java.util.List;

public abstract class Promotion {
    protected final List<String> barcodes;

    public Promotion(String[] barcodes) {
        this.barcodes = Arrays.asList(barcodes);
    }

    public List<String> getBarcodes() {
        return barcodes;
    }

    boolean supports(Item item) {
        return getBarcodes().contains(item.getProductCode());
    }

    public abstract void apply(Item item);

    boolean supports(Order order) {
        return order.getItems().stream().anyMatch(this::supports);
    }

    public abstract boolean shouldPrintSavingForItem(Item item);

    public abstract boolean shouldPrintSavingInSummary(Order order);

    public abstract boolean shouldPrintPromotionSummary(Order order);
}
