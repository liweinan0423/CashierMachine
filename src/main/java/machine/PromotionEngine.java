package machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PromotionEngine {
    List<Promotion> promotions = new ArrayList<>();

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

    public Promotion getBuyXGetYFreePromotion() {
        Optional<Promotion> promotion = getPromotions().stream().filter(p -> p instanceof BuyXGetYFreePromotion).findFirst();
        return promotion.isPresent() ? promotion.get() : new NOPPromotion();
    }

    void buildPromotionSummary(StringBuilder receiptBuilder, Order order) {
        receiptBuilder.append("买二赠一商品:\n");
        order.getItems().stream().filter(getBuyXGetYFreePromotion()::supports).forEach(item -> receiptBuilder.append(String.format("名称: %s, 数量: %d%s\n", item.getName(), item.getFreeQuantity(), item.getUnit())));
    }
}
