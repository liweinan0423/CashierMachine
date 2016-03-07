package machine;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CashierMachine {

    private static Gson GSON = new Gson();

    private String storeName;
    private StringBuilder receiptBuilder;
    private Map<String, Product> catalog;
    private PercentagePromotion percentagePromotion;
    private BuyXGetYFreePromotion buyXGetYFreePromotion;

    private Order order;

    public CashierMachine(String storeName, Map<String, Product> catalog) {
        this.storeName = storeName;
        this.catalog = catalog;
    }

    public void start() {
        order = new Order();
        receiptBuilder = new StringBuilder();
    }

    public void scan(String input) {
        parse(input).forEach(this::addItem);
    }

    private void addItem(String barcodeString) {
        Barcode barcode = new Barcode(barcodeString);
        Product product = catalog.get(barcode.getProductCode());
        order.addItem(product, barcode.getQuantity());
    }

    private List<String> parse(String input) {
        return GSON.<List<String>>fromJson(input, List.class);
    }

    private Item createItem(Barcode barcode) {
        String productCode = barcode.getProductCode();
        int quantity = barcode.getQuantity();

        Product product = catalog.get(productCode);
        Item item = new Item(product);
        item.setQuantity(quantity);
        return item;
    }

    public void calculate() {
        order.getItems().forEach(Item::calculate);
        if (order.getItems().stream().anyMatch(item -> getPercentagePromotionBarcodes().contains(item.getProductCode()))) {
            order.getItems().stream().filter(item -> getPercentagePromotionBarcodes().contains(item.getProductCode())).forEach(item -> item.applyPercentagePromotion(getPercentage()));
        }
        if (order.getItems().stream().anyMatch(item -> getBuyXGetYFreePromotionBarcodes().contains(item.getProductCode()))) {
            order.getItems().stream().filter(item -> getBuyXGetYFreePromotionBarcodes().contains(item.getProductCode())).forEach(item -> item.applyBuyXGetYFreePromotion(getX(), getY()));
        }
        this.order.setTotalPrice(order.getItems().stream().mapToDouble(Item::getTotalPayable).sum());
        this.order.setTotalSaving(order.getItems().stream().mapToDouble(Item::getSaving).sum());
    }

    public String print() {
        printHeader();
        printItems();
        printDelimiter();
        printPromotionSummary();
        printSummary();
        printFooter();
        return receiptBuilder.toString();
    }

    private void printHeader() {
        receiptBuilder.append("***<").append(storeName).append(">购物清单***\n");
    }

    private void printItems() {
        order.getItems().forEach(this::print);
    }

    private void print(Item item) {
        if (hasPercentagePromotion(item)) {
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

    private void printPromotionSummary() {
        if (hasBuyXGetYFreePromotion()) {
            receiptBuilder.append("买二赠一商品:\n");
            order.getItems().stream().filter(item -> getBuyXGetYFreePromotionBarcodes().contains(item.getProductCode())).forEach(item -> {
                receiptBuilder.append(String.format("名称: %s, 数量: %d%s\n", item.getName(), item.getFreeQuantity(), item.getUnit()));
            });
            printDelimiter();
        }
    }

    private void printDelimiter() {
        receiptBuilder.append("----------\n");
    }

    private void printFooter() {
        receiptBuilder.append("**********\n");
    }

    private void printSummary() {
        receiptBuilder.append(String.format("总计: %.2f(元)\n", order.getTotalPrice()));
        if (hasPercentagePromotion() || hasBuyXGetYFreePromotion()) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n", order.getTotalSaving()));
        }
    }

    private boolean hasPercentagePromotion() {
        return order.getItems().stream().anyMatch(item -> getPercentagePromotionBarcodes().contains(item.getProductCode()));
    }

    private boolean hasBuyXGetYFreePromotion() {
        return order.getItems().stream().anyMatch(item -> getBuyXGetYFreePromotionBarcodes().contains(item.getProductCode()));
    }

    private boolean hasPercentagePromotion(Item item) {
        return getPercentagePromotionBarcodes().contains(item.getProductCode());
    }

    public void reset() {

    }

    public void setUpPercentagePromotion(double percentage, String... barcodes) {
        percentagePromotion = new PercentagePromotion(percentage, barcodes);
    }

    public double getPercentage() {
        if (percentagePromotion != null) {
            return percentagePromotion.getPercentage();
        } else {
            return 1;
        }
    }

    public List<String> getPercentagePromotionBarcodes() {
        if (percentagePromotion != null) {
            return percentagePromotion.getBarcodes();
        } else {
            return new ArrayList<>();
        }
    }


    public void setUpBuyXGetYFreePromotion(int x, int y, String... barcodes) {
        buyXGetYFreePromotion = new BuyXGetYFreePromotion(x, y, barcodes);
    }

    public int getX() {
        if (buyXGetYFreePromotion != null) {
            return buyXGetYFreePromotion.getX();
        } else {
            return 0;
        }
    }

    public int getY() {
        return buyXGetYFreePromotion.getY();
    }

    public List<String> getBuyXGetYFreePromotionBarcodes() {
        if (buyXGetYFreePromotion != null) {
            return buyXGetYFreePromotion.getBarcodes();
        } else {
            return new ArrayList<>();
        }
    }

}
