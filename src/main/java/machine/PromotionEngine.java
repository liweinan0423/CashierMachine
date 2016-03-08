package machine;

import java.util.ArrayList;
import java.util.List;

public class PromotionEngine {
    private List<Promotion> promotions = new ArrayList<>();

    public PromotionEngine() {
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public boolean addPromotion(Promotion percentagePromotion) {
        return getPromotions().add(percentagePromotion);
    }

    void apply(Item item) {
        getPromotions().forEach(promotion -> {
            if (promotion.supports(item)) {
                promotion.apply(item);
            }
        });
    }

    public void apply(Order order) {
        order.getItems().forEach(this::apply);
    }

    boolean shouldPrintSavingInSummary(Order order) {
        return promotions.stream().anyMatch(p -> p.shouldPrintSavingInSummary(order));
    }

    boolean shouldPrintPromotionSummary(Order order) {
        return promotions.stream().anyMatch(p -> p.shouldPrintPromotionSummary(order));
    }

    boolean shouldPrintSavingOnItem(Item item) {
        return promotions.stream().anyMatch(p -> p.shouldPrintSavingForItem(item));
    }

    void buildPromotionSummary(StringBuilder receiptBuilder, Order order) {
        promotions.forEach(promotion -> {
            if (promotion.shouldPrintPromotionSummary(order)) {
                promotion.buildPromotionSummary(receiptBuilder, order);
            }
        });
    }
}
