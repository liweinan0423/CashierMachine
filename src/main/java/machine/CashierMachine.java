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
    private double totalSaving;
    private List<String> buyXGetYFreePromotionBarcodes = new ArrayList<>();
    private int x;
    private int y;
    private PercentagePromotion percentagePromotion;

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
        if (items.stream().anyMatch(item -> getPercentagePromotionBarcodes().contains(item.getBarcode()))) {
            items.stream().filter(item -> getPercentagePromotionBarcodes().contains(item.getBarcode())).forEach(item -> item.applyPercentagePromotion(getPercentage()));
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

    private void printHeader() {
        receiptBuilder.append("***<").append(storeName).append(">购物清单***\n");
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

    private void printPromotionSummary() {
        if (hasBuyXGetYFreePromotion()) {
            receiptBuilder.append("买二赠一商品:\n");
            items.stream().filter(item -> buyXGetYFreePromotionBarcodes.contains(item.getBarcode())).forEach(item -> {
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
        receiptBuilder.append(String.format("总计: %.2f(元)\n", totalPrice));
        if (hasPercentagePromotion() || hasBuyXGetYFreePromotion()) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n", totalSaving));
        }
    }

    private boolean hasPercentagePromotion() {
        return items.stream().anyMatch(item -> getPercentagePromotionBarcodes().contains(item.getBarcode()));
    }

    private boolean hasBuyXGetYFreePromotion() {
        return items.stream().anyMatch(item -> buyXGetYFreePromotionBarcodes.contains(item.getBarcode()));
    }

    private boolean hasPercentagePromotion(Item item) {
        return getPercentagePromotionBarcodes().contains(item.getBarcode());
    }

    public void reset() {

    }

    public void setUpBuyXGetYFreePromotion(int x, int y, String... barcodes) {
        this.x = x;
        this.y = y;
        this.buyXGetYFreePromotionBarcodes = Arrays.asList(barcodes);
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
}
