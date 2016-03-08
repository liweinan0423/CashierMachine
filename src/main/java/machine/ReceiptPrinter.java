package machine;

public class ReceiptPrinter {

    private final String storeName;
    private Order order;
    private StringBuilder receiptBuilder;

    public ReceiptPrinter(String storeName, Order order) {
        this.storeName = storeName;
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
        if (printSavingOnItem(item)) {
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

    private boolean printSavingOnItem(Item item) {
        return item.getPromotion() instanceof PercentagePromotion;
    }

    void printDelimiter() {
        receiptBuilder.append("----------\n");
    }

    void printPromotionSummary() {
        if (shouldPrintPromotionSummary()) {
            BuyXGetYFreePromotion promotion = (BuyXGetYFreePromotion) order.getItems().stream().filter(item -> item.getPromotion() instanceof BuyXGetYFreePromotion).findFirst().get().getPromotion();
            receiptBuilder.append("买二赠一商品:\n");
            order.getItems().stream().filter(promotion::supports).forEach(item ->
                    receiptBuilder.append(String.format("名称: %s, 数量: %d%s\n", item.getName(), item.getFreeQuantity(), item.getUnit())));
            printDelimiter();
        }
    }

    private boolean shouldPrintPromotionSummary() {
        return order.getItems().stream().anyMatch(item -> item.getPromotion() instanceof BuyXGetYFreePromotion);
    }

    void printSummary() {
        receiptBuilder.append(String.format("总计: %.2f(元)\n",order.getTotalPrice()));
        if (shouldPrintSavingInSummary()) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n",order.getTotalSaving()));
        }
    }

    private boolean shouldPrintSavingInSummary() {
        return order.getItems().stream().anyMatch(item -> item.getPromotion() != null);
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
