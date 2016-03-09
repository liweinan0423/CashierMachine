package machine.order;

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

    public void addItem(Product product, int quantity) {
        Optional<Item> optional = findItemWith(product);
        if (optional.isPresent()) {
            optional.get().increaseBy(quantity);
        } else {
            getItems().add(new Item(product, quantity));
        }
    }

    private Optional<Item> findItemWith(Product product) {
        return getItems().stream().filter(item -> item.getProduct().equals(product)).findFirst();
    }

    public void calculate() {
        getItems().forEach(Item::calculate);
    }
}
