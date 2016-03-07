package machine;

public class Item {
    private final Product product;
    private double subTotal;
    private int quantity;
    private double totalPayable;

    public Item(Product product) {
        this.product = product;
        setQuantity(1);
    }

    public void calculate() {
        this.totalPayable = this.subTotal = getPrice() * quantity;
    }

    public String getName() {
        return product.getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return product.getUnit();
    }

    public double getPrice() {
        return product.getPrice();
    }

    public String getProductCode() {
        return product.getProductCode();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void applyPercentagePromotion(double percentage) {
        totalPayable = subTotal * percentage;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public double getSaving() {
        return subTotal - totalPayable;
    }

    public void applyBuyXGetYFreePromotion(int x, int y) {
        setQuantity(quantity + quantity / x * y);
        subTotal = quantity * getPrice();
    }

    public int getFreeQuantity() {
        return (int) (getSaving() / getPrice());
    }
}
