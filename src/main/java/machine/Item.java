package machine;

public class Item {
    private final String barcode;
    private final String name;
    private final double price;
    private final String unit;
    private double subTotal;
    private int quantity;

    public Item(String barcode, String name, double price, String unit) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.unit = unit;
        setQuantity(1);
    }

    public void calculate() {
        this.subTotal = price * quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
