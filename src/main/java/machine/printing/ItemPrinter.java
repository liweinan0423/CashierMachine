package machine.printing;

import machine.order.Item;
import machine.promotion.PercentagePromotion;

import java.io.PrintStream;

public class ItemPrinter implements Printable {

    private Item item;

    public ItemPrinter(Item item) {
        this.item = item;
    }

    @Override
    public void print(PrintStream stream) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("名称: %s, 数量: %d%s, 单价: %.2f(元), 小计: %.2f(元)",
                item.getProduct().getName(),
                item.getQuantity(),
                item.getProduct().getUnit(),
                item.getProduct().getPrice(),
                item.getTotalPayable()));

        if (shouldPrintSaving()) {
            builder.append(String.format(", 节省%.2f(元)", item.getSaving()));
        }
        stream.println(builder.toString());
    }

    private boolean shouldPrintSaving() {
        return item.getPromotion() != null && item.getPromotion() instanceof PercentagePromotion;
    }
}
