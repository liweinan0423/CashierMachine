package machine;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CashierMachine {

    private static Gson GSON = new Gson();

    private String storeName;
    private List<Item> items;
    private double totalPrice;
    private StringBuilder receiptBuilder;

    public CashierMachine(String storeName) {
        this.storeName = storeName;
    }

    public void scan(String input) {
        List<String> barcodes = GSON.<List<String>>fromJson(input, List.class);
        barcodes.forEach(barcode -> {
            Item item = new Item(barcode, "可口可乐", 3.00, "瓶", 1);
            items.add(item);
        });
    }

    public void start() {
        items = new ArrayList<>();
        receiptBuilder = new StringBuilder();
    }

    public void calculate() {
        items.forEach(Item::calculate);
        totalPrice = items.stream().mapToDouble(Item::getSubTotal).sum();
    }

    public String print() {
        printHeader();
        printItems();
        printDelimiter();
        printSummary();
        printFooter();
        return receiptBuilder.toString();
    }

    private void printDelimiter() {
        receiptBuilder.append("----------\n");
    }

    private void printFooter() {
        receiptBuilder.append("**********\n");
    }

    private void printSummary() {
        receiptBuilder.append(String.format("总计: %.2f(元)\n", totalPrice));
    }

    private void printItems() {
        items.forEach(this::print);
    }

    private void print(Item item) {
        receiptBuilder.append(String.format("名称: %s, 数量: %d%s, 单价: %.2f(元), 小计: %.2f(元)\n",
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.getPrice(),
                item.getSubTotal()
                ));
    }

    private void printHeader() {
        receiptBuilder.append("***<").append(storeName).append(">购物清单***\n");
    }

    public void reset() {

    }
}
