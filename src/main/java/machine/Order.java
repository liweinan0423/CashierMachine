package machine;

import java.util.List;

public class Order {
    List<Item> items;
    double totalPrice;
    double totalSaving;

    public Order() {
    }

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
}
