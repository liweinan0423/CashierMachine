package machine;

public class Barcode {
    private String productCode;
    private int quantity;

    public Barcode(String barcodeString) {
        String[] split = barcodeString.split("-");
        productCode = split[0];
        quantity = Integer.parseInt(split.length == 2 ? split[1] : "1");

    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantity() {
        return quantity;
    }
}
