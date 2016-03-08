package machine;

public class ReceiptPrinter {

    private final String storeName;
    private final PromotionEngine promotionEngine;
    private Order order;
    private StringBuilder receiptBuilder;

    public ReceiptPrinter(String storeName, PromotionEngine promotionEngine, Order order) {
        this.storeName = storeName;
        this.promotionEngine = promotionEngine;
        this.order = order;
        receiptBuilder = new StringBuilder();
    }

    void printHeader() {
        receiptBuilder.append("***<").append(storeName).append(">购物清单***\n");
    }

    void printItems() {
        order.getItems().forEach(this::print);
    }

    private void print(Item item) {
        if (promotionEngine.shouldPrintSavingOnItem(item)) {
            receiptBuilder.append(String.format("名称: %s, 数量: %d%s, 单价: %.2f(元), 小计: %.2f(元), 节省%.2f(元)\n",
                    item.getName(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getPrice(),
                    item.getTotalPayable(),
                    item.getSaving()
            ));
        } else {
            receiptBuilder.append(String.format("名称: %s, 数量: %d%s, 单价: %.2f(元), 小计: %.2f(元)\n",
                    item.getName(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getPrice(),
                    item.getTotalPayable()
            ));
        }
    }

    void printDelimiter() {
        receiptBuilder.append("----------\n");
    }

    void printPromotionSummary() {
        if (promotionEngine.shouldPrintPromotionSummary(order)) {
            promotionEngine.buildPromotionSummary(receiptBuilder,order);
            printDelimiter();
        }
    }

    void printSummary() {
        receiptBuilder.append(String.format("总计: %.2f(元)\n",order.getTotalPrice()));
        if (promotionEngine.shouldPrintSavingInSummary(order)) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n",order.getTotalSaving()));
        }
    }

    void printFooter() {
        receiptBuilder.append("**********\n");
    }

    public String build() {
        return receiptBuilder.toString();
    }

    public void printReceipt() {
        printHeader();
        printItems();
        printDelimiter();
        printPromotionSummary();
        printSummary();
        printFooter();
    }
}
