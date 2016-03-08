package machine;

public class BuyXGetYFreePromotion extends Promotion {
    private final int x;
    private final int y;

    public BuyXGetYFreePromotion(int x, int y, String... productCodes) {
        super(productCodes);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void apply(Item item) {
        item.setQuantity(item.getQuantity() + item.getQuantity() / getX() * getY());
        item.setSubTotal(item.getQuantity() * item.getPrice());
    }

    @Override
    public boolean shouldPrintSavingForItem(Item item) {
        return false;
    }

    @Override
    public boolean shouldPrintSavingInSummary(Order order) {
        return true;
    }

    @Override
    public boolean shouldPrintPromotionSummary(Order order) {
        return true;
    }

    @Override
    public void buildPromotionSummary(StringBuilder receiptBuilder, Order order) {
        receiptBuilder.append("买二赠一商品:\n");
        order.getItems().stream().filter(this::supports).forEach(item ->
                receiptBuilder.append(String.format("名称: %s, 数量: %d%s\n", item.getName(), item.getFreeQuantity(), item.getUnit())));
    }

}
