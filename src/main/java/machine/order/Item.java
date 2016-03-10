package machine.order;

import machine.product.Product;
import machine.promotion.Promotion;

public class Item {
    private final Product product;
    private int quantity;
    private double subTotal;
    private double saving;
    private Promotion promotion;

    public Item(Product product) {
        this.product = product;
        setQuantity(1);
    }

    public Item(Product product, int quantity) {
        this(product);
        setQuantity(quantity);
    }

    /**
     * calculate price before applying any promotion
     */
    public void calculate() {
        this.setSubTotal(getProduct().getPrice() * getQuantity());
    }

    public void increaseBy(int quantity) {
        setQuantity(getQuantity() + quantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductCode() {
        return getProduct().getProductCode();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSaving() {
        return saving;
    }

    public void setSaving(double saving) {
        this.saving = saving;
    }

    public Product getProduct() {
        return product;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
