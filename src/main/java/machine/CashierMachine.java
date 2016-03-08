package machine;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class CashierMachine {

    private static Gson GSON = new Gson();
    final PromotionEngine promotionEngine = new PromotionEngine();

    private String storeName;
    public StringBuilder receiptBuilder;
    private Map<String, Product> catalog;

    private Order order;

    public CashierMachine(String storeName, Map<String, Product> catalog) {
        this.storeName = storeName;
        this.catalog = catalog;
    }

    public void start() {
        setOrder(new Order());
        receiptBuilder = new StringBuilder();
    }

    public void scan(String input) {
        parse(input).forEach(this::addItem);
    }

    private void addItem(String barcodeString) {
        Barcode barcode = new Barcode(barcodeString);
        Product product = catalog.get(barcode.getProductCode());
        getOrder().addItem(product, barcode.getQuantity());
    }

    private List<String> parse(String input) {
        return GSON.<List<String>>fromJson(input, List.class);
    }

    public void calculate() {
        getOrder().getItems().forEach(Item::calculate);
        promotionEngine.apply(getOrder());
        this.getOrder().setTotalPrice(getOrder().getItems().stream().mapToDouble(Item::getTotalPayable).sum());
        this.getOrder().setTotalSaving(getOrder().getItems().stream().mapToDouble(Item::getSaving).sum());
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
        getOrder().getItems().forEach(this::print);
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

    private void printPromotionSummary() {
        if (promotionEngine.shouldPrintPromotionSummary(getOrder())) {
            promotionEngine.buildPromotionSummary(receiptBuilder, getOrder());
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
        receiptBuilder.append(String.format("总计: %.2f(元)\n", getOrder().getTotalPrice()));
        if (promotionEngine.shouldPrintSavingInSummary(getOrder())) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n", getOrder().getTotalSaving()));
        }
    }

    public void reset() {

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
