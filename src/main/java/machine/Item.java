package machine;

public class Item {
    private final String barcode;
    private final String name;
    private final double price;
    private final String unit;
    private double subTotal;
    private int quantity;
    private double totalPayable;
    private Object saving;

    public Item(String barcode, String name, double price, String unit) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.unit = unit;
        setQuantity(1);
    }

    public void calculate() {
        this.totalPayable = this.subTotal = price * quantity;
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

    public void applyPercentagePromotion(double percentage) {
        totalPayable = subTotal * percentage;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public double getSaving() {
        return subTotal - totalPayable;
    }
}
