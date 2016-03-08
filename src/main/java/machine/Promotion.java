package machine;

import java.util.Arrays;
import java.util.List;

public abstract class Promotion {
    protected final List<String> productCodes;

    public Promotion(String... productCodes) {
        this.productCodes = Arrays.asList(productCodes);
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    boolean supports(Item item) {
        return getProductCodes().contains(item.getProductCode());
    }

    public abstract void apply(Item item);

    public abstract boolean shouldPrintSavingForItem(Item item);

    public abstract boolean shouldPrintSavingInSummary(Order order);

    public abstract boolean shouldPrintPromotionSummary(Order order);
}
