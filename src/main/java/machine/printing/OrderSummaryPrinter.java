package machine.printing;

import machine.Order;

import java.io.PrintStream;

public class OrderSummaryPrinter implements Printable {
    private Order order;

    public OrderSummaryPrinter(Order order) {
        this.order = order;
    }

    @Override
    public void print(PrintStream stream) {
        stream.println(String.format("总计: %.2f(元)", order.getTotalPrice()));
        if (hasAnySaving()) {
            stream.println(String.format("节省: %.2f(元)", order.getTotalSaving()));
        }
    }

    private boolean hasAnySaving() {
        return order.getTotalSaving() > 0;
    }
}
