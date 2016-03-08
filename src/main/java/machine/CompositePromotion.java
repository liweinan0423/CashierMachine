package machine;

public class CompositePromotion extends Promotion {

    private final Promotion highPriority;
    private final Promotion lowPriority;

    public CompositePromotion(Promotion highPriority, Promotion lowPriority) {
        this.highPriority = highPriority;
        this.lowPriority = lowPriority;
    }


    @Override
    boolean supports(Item item) {
        return highPriority.supports(item) || lowPriority.supports(item);
    }

    @Override
    public void apply(Item item) {
        if (highPriority.supports(item) && lowPriority.supports(item)) {
            highPriority.apply(item);
        } else if (highPriority.supports(item) && !lowPriority.supports(item)){
            highPriority.apply(item);
        } else if (!highPriority.supports(item) && lowPriority.supports(item)) {
            lowPriority.apply(item);
        }
    }

    @Override
    public boolean shouldPrintSavingForItem(Item item) {
        if (highPriority.supports(item) && lowPriority.supports(item)) {
            return highPriority.shouldPrintSavingForItem(item);
        } else if (highPriority.supports(item) && !lowPriority.supports(item)){
            return highPriority.shouldPrintSavingForItem(item);
        } else if (!highPriority.supports(item) && lowPriority.supports(item)) {
            return lowPriority.shouldPrintSavingForItem(item);
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldPrintSavingInSummary(Order order) {
        if (order.getItems().stream().anyMatch(highPriority::supports) && order.getItems().stream().anyMatch(lowPriority::supports)) {
            return highPriority.shouldPrintSavingInSummary(order);
        } else if (order.getItems().stream().anyMatch(highPriority::supports) && !order.getItems().stream().anyMatch(lowPriority::supports)) {
            return highPriority.shouldPrintSavingInSummary(order);
        } else if (!order.getItems().stream().anyMatch(highPriority::supports) && order.getItems().stream().anyMatch(lowPriority::supports)) {
            return lowPriority.shouldPrintSavingInSummary(order);
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldPrintPromotionSummary(Order order) {
        if (order.getItems().stream().anyMatch(highPriority::supports) && order.getItems().stream().anyMatch(lowPriority::supports)) {
            return highPriority.shouldPrintPromotionSummary(order);
        } else if (order.getItems().stream().anyMatch(highPriority::supports) && !order.getItems().stream().anyMatch(lowPriority::supports)) {
            return highPriority.shouldPrintPromotionSummary(order);
        } else if (!order.getItems().stream().anyMatch(highPriority::supports) && order.getItems().stream().anyMatch(lowPriority::supports)) {
            return lowPriority.shouldPrintPromotionSummary(order);
        } else {
            return false;
        }
    }

    @Override
    public void buildPromotionSummary(StringBuilder receiptBuilder, Order order) {
        if (order.getItems().stream().anyMatch(highPriority::supports) && order.getItems().stream().anyMatch(lowPriority::supports)) {
            highPriority.buildPromotionSummary(receiptBuilder, order);
        } else if (order.getItems().stream().anyMatch(highPriority::supports) && !order.getItems().stream().anyMatch(lowPriority::supports)) {
            highPriority.buildPromotionSummary(receiptBuilder, order);
        } else if (!order.getItems().stream().anyMatch(highPriority::supports) && order.getItems().stream().anyMatch(lowPriority::supports)) {
            lowPriority.buildPromotionSummary(receiptBuilder, order);
        }
    }
}
