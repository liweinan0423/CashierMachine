package machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    List<Item> items = new ArrayList<>();
    double totalPrice;
    double totalSaving;

    public List<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalSaving() {
        return totalSaving;
    }

    public void setTotalSaving(double totalSaving) {
        this.totalSaving = totalSaving;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Barcode barcode, Item item) {
        if (hasItemWithSameProduct(barcode)) {
            increaseQuantity(barcode);
        } else {
            getItems().add(item);
        }
    }

    private boolean hasItemWithSameProduct(Barcode barcode) {
        return getItems().stream().anyMatch(i -> i.getProductCode().equals(barcode.getProductCode()));
    }

    private void increaseQuantity(Barcode barcode) {
        Optional<Item> first = getItems().stream().filter(i -> i.getProductCode().equals(barcode.getProductCode())).findFirst();
        first.get().setQuantity(first.get().getQuantity() + barcode.getQuantity());
    }
}
