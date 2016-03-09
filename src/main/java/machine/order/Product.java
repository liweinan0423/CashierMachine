package machine.order;

public class Product {
    private final String productCode;
    private final String name;
    private final double price;
    private final String unit;

    public Product(String productCode, String name, double price, String unit) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }
}
