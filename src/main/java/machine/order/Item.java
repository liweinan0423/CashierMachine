package machine.order;

import machine.promotion.Promotion;

public class Item {
    private final Product product;
    private double subTotal;
    private int quantity;
    private double totalPayable;
    private Promotion promotion;

    public Item(Product product) {
        this.product = product;
        setQuantity(1);
    }

    public Item(Product product, int quantity) {
        this(product);
        setQuantity(quantity);
    }

    public void calculate() {
        this.setSubTotal(getPrice() * getQuantity());
        this.setTotalPayable(getPrice() * getQuantity());
    }

    public String getName() {
        return getProduct().getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return getProduct().getUnit();
    }

    public double getPrice() {
        return getProduct().getPrice();
    }

    public String getProductCode() {
        return getProduct().getProductCode();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getTotalPayable() {
        return totalPayable;
    }

    public double getSaving() {
        return getSubTotal() - getTotalPayable();
    }

    public int getFreeQuantity() {
        return (int) (getSaving() / getPrice());
    }

    public Product getProduct() {
        return product;
    }

    void increaseBy(int quantity) {
        setQuantity(getQuantity() + quantity);
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
