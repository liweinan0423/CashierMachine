package com.thoughtworks.cashiermachine.printing;

import com.thoughtworks.cashiermachine.printing.internal.PrintLn;
import com.thoughtworks.cashiermachine.order.Item;
import com.thoughtworks.cashiermachine.order.Order;
import com.thoughtworks.cashiermachine.printing.internal.SequentialPrintable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptPrinterBuilder {

    private List<Printable> printableList;
    private Order order;

    public ReceiptPrinterBuilder(Order order) {
        this.order = order;
        printableList = new ArrayList<>();
    }

    public ReceiptPrinterBuilder printHeader(String header) {
        printableList.add(new PrintLn(header));
        return this;
    }

    public ReceiptPrinterBuilder printItems() {
        printableList.add(new SequentialPrintable(
                order.getItems().stream().map(this::itemPrinter).collect(Collectors.toList()))
        );
        return printDelimiter();
    }

    private ItemPrinter itemPrinter(Item item) {
        return new ItemPrinter(item);
    }

    private ReceiptPrinterBuilder printDelimiter() {
        printableList.add(new PrintLn("----------"));
        return this;
    }

    public ReceiptPrinterBuilder printPromotionSummary() {
        printableList.add(new PromotionSummaryPrinter(order));
        return this;
    }

    public ReceiptPrinterBuilder printOrderSummary() {
        printableList.add(new OrderSummaryPrinter(order));
        return this;
    }

    public ReceiptPrinterBuilder printFooter(String footer) {
        printableList.add(new PrintLn(footer));
        return this;
    }

    public Printable build() {
        return new SequentialPrintable(printableList);
    }
}
