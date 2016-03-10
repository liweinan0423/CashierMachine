package machine.printing;

import machine.order.Item;
import machine.order.Order;
import machine.promotion.BuyXGetYFreePromotion;

import java.io.PrintStream;

public class PromotionSummaryPrinter implements Printable {

    private Order order;

    public PromotionSummaryPrinter(Order order) {
        this.order = order;
    }

    @Override
    public void print(PrintStream stream) {
        if (hasBuyXGetYFreePromotion()) {
            stream.println("买二赠一商品:");
            order.getItems().stream()
                    .filter(item -> item.getPromotion() != null && item.getPromotion() instanceof BuyXGetYFreePromotion)
                    .forEach(item -> stream.println(String.format("名称: %s, 数量: %d%s", item.getProduct().getName(), freeQuantity(item), item.getProduct().getUnit())));
            stream.println("----------");
        }
    }

    private int freeQuantity(Item item) {
        return (int) (item.getSaving() / item.getProduct().getPrice());
    }

    private boolean hasBuyXGetYFreePromotion() {
        return order.getItems().stream().anyMatch(item -> item.getPromotion() != null && item.getPromotion() instanceof BuyXGetYFreePromotion);
    }
}