package machine;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class CashierMachine {

    private static Gson GSON = new Gson();
    private PromotionEngine promotionEngine;

    private String storeName;
    public StringBuilder receiptBuilder;
    private Map<String, Product> catalog;

    private Order order;
    private ReceiptPrinter printer;

    public CashierMachine(String storeName, Map<String, Product> catalog) {
        this.storeName = storeName;
        this.catalog = catalog;
        this.promotionEngine = new PromotionEngine();
    }

    public void start() {
        this.order = new Order();
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

    public void calculate() {
        order.calculate();
        promotionEngine.apply(order);
        order.setTotalPrice(order.getItems().stream().mapToDouble(Item::getTotalPayable).sum());
        order.setTotalSaving(order.getItems().stream().mapToDouble(Item::getSaving).sum());
    }

    public String print() {
        printer = new ReceiptPrinter(storeName, order);
        printer.printReceipt();
        return printer.build();
    }

    public void addPromotion(Promotion promotion) {
        promotionEngine.addPromotion(promotion);
    }
}
