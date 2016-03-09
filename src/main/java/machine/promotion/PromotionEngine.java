package machine.promotion;

import machine.order.Item;
import machine.order.Order;

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

}
