package machine;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
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
    private double percentage;
    private List<String> percentagePromotionBarcodes = new ArrayList<>();
    private double totalSaving;
    private List<String> buyXGetYFreePromotionBarcodes = new ArrayList<>();
    private int x;
    private int y;

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
        if (items.stream().anyMatch(item -> percentagePromotionBarcodes.contains(item.getBarcode()))) {
            items.stream().filter(item -> percentagePromotionBarcodes.contains(item.getBarcode())).forEach(item -> item.applyPercentagePromotion(percentage));
        }
        if (items.stream().anyMatch(item -> buyXGetYFreePromotionBarcodes.contains(item.getBarcode()))) {
            items.stream().filter(item -> buyXGetYFreePromotionBarcodes.contains(item.getBarcode())).forEach(item -> item.applyBuyXGetYFreePromotion(x, y));
        }
        totalPrice = items.stream().mapToDouble(Item::getTotalPayable).sum();
        totalSaving = items.stream().mapToDouble(Item::getSaving).sum();
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

    private void printPromotionSummary() {
        if (hasBuyXGetYFreePromotion()) {
            receiptBuilder.append("买二赠一商品:\n");
            items.stream().filter(item -> buyXGetYFreePromotionBarcodes.contains(item.getBarcode())).forEach(item -> {
                receiptBuilder.append(String.format("名称: %s, 数量: %d%s\n", item.getName(), item.getFreeQuantity(), item.getUnit()));
            });
            printDelimiter();
        }
    }

    private boolean hasBuyXGetYFreePromotion() {
        return items.stream().anyMatch(item -> buyXGetYFreePromotionBarcodes.contains(item.getBarcode()));
    }

    private void printDelimiter() {
        receiptBuilder.append("----------\n");
    }

    private void printFooter() {
        receiptBuilder.append("**********\n");
    }

    private void printSummary() {
        receiptBuilder.append(String.format("总计: %.2f(元)\n", totalPrice));
        if (hasPercentagePromotion() || hasBuyXGetYFreePromotion()) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n", totalSaving));
        }
    }

    private boolean hasPercentagePromotion() {
        return items.stream().anyMatch(this::hasPercentagePromotion);
    }

    private void printItems() {
        items.forEach(this::print);
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

    private boolean hasPercentagePromotion(Item item) {
        return percentagePromotionBarcodes.contains(item.getBarcode());
    }

    private void printHeader() {
        receiptBuilder.append("***<").append(storeName).append(">购物清单***\n");
    }

    public void reset() {

    }

    public void setUpPercentagePromotion(double percentage, String... barcodes) {
        this.percentage = percentage;
        this.percentagePromotionBarcodes = Arrays.asList(barcodes);
    }

    public void setUpBuyXGetYFreePromotion(int x, int y, String... barcodes) {
        this.x = x;
        this.y = y;
        this.buyXGetYFreePromotionBarcodes = Arrays.asList(barcodes);
    }
}
