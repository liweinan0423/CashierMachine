package machine;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CashierMachine {

    private static Gson GSON = new Gson();

    private String storeName;
    private List<Item> items;
    private double totalPrice;
    private StringBuilder receiptBuilder;
    private Map<String, Item> catalog;

    public CashierMachine(String storeName, Map<String, Item> itemsStore) {
        this.storeName = storeName;
        this.catalog = itemsStore;
    }

    public void scan(String input) {
        List<String> barcodes = GSON.<List<String>>fromJson(input, List.class);
        barcodes.forEach(barcode -> {
            Item item = createItem(barcode);
            if (items.stream().anyMatch(i -> i.getBarcode().equals(barcode))) {
                Optional<Item> first = items.stream().filter(i -> i.getBarcode().equals(barcode)).findFirst();
                first.get().setQuantity(first.get().getQuantity() + 1);
            } else {
                items.add(item);
            }
        });
    }

    private Item createItem(String barcode) {
        String[] split = barcode.split("-");
        if (split.length == 1) {
            Item prototype = catalog.get(barcode);
            return new Item(prototype.getBarcode(), prototype.getName(), prototype.getPrice(), prototype.getUnit());
        } else {
            String code = split[0];
            int quantity = Integer.parseInt(split[1]);
            Item prototype = catalog.get(code);
            Item item = new Item(prototype.getBarcode(), prototype.getName(), prototype.getPrice(), prototype.getUnit());
            item.setQuantity(quantity);
            return item;
        }
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
