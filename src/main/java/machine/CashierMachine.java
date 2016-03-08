package machine;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CashierMachine {

    private static Gson GSON = new Gson();

    private String storeName;
    private StringBuilder receiptBuilder;
    private Map<String, Product> catalog;
    List<Promotion> promotions = new ArrayList<>();

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
        applyPromotions();
        this.getOrder().setTotalPrice(getOrder().getItems().stream().mapToDouble(Item::getTotalPayable).sum());
        this.getOrder().setTotalSaving(getOrder().getItems().stream().mapToDouble(Item::getSaving).sum());
    }

    private void applyPromotions() {
        getOrder().getItems().forEach(item -> {
            promotions.forEach(promotion -> {
                if (promotion.supports(item)) {
                    promotion.apply(item);
                }
            });
        });
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
        if (getPercentagePromotion().shouldPrintSavingInForItem(item)) {
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
        if (getBuyXGetYFreePromotion().supports(getOrder())) {
            receiptBuilder.append("买二赠一商品:\n");
            getOrder().getItems().stream().filter(getBuyXGetYFreePromotion()::supports).forEach(item -> receiptBuilder.append(String.format("名称: %s, 数量: %d%s\n", item.getName(), item.getFreeQuantity(), item.getUnit())));
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
        if (orderHasAnySaving()) {
            receiptBuilder.append(String.format("节省: %.2f(元)\n", getOrder().getTotalSaving()));
        }
    }

    private boolean orderHasAnySaving() {
        return getPercentagePromotion().supports(getOrder()) || getBuyXGetYFreePromotion().supports(getOrder());
    }

    public void reset() {

    }

    public boolean addPromotion(Promotion percentagePromotion) {
        return promotions.add(percentagePromotion);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Promotion getPercentagePromotion() {
        Optional<Promotion> promotion = promotions.stream().filter(p -> p instanceof PercentagePromotion).findFirst();
        return promotion.isPresent() ? promotion.get() : new NOPPromotion();
    }

    public Promotion getBuyXGetYFreePromotion() {
        Optional<Promotion> promotion = promotions.stream().filter(p -> p instanceof BuyXGetYFreePromotion).findFirst();
        return promotion.isPresent() ? promotion.get() : new NOPPromotion();
    }
}
