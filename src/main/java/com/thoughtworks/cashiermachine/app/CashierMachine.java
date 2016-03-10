package com.thoughtworks.cashiermachine.app;

import com.thoughtworks.cashiermachine.printing.ReceiptPrinterBuilder;
import com.thoughtworks.cashiermachine.product.ProductCatalog;
import com.google.gson.Gson;
import com.thoughtworks.cashiermachine.order.Barcode;
import com.thoughtworks.cashiermachine.order.Order;
import com.thoughtworks.cashiermachine.printing.Printable;
import com.thoughtworks.cashiermachine.product.Product;
import com.thoughtworks.cashiermachine.promotion.PromotionEngine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class CashierMachine {

    private static Gson GSON = new Gson();
    private PromotionEngine promotionEngine;

    private ProductCatalog catalog;

    private Order order;

    public CashierMachine(ProductCatalog catalog, PromotionEngine promotionEngine) {
        this.catalog = catalog;
        this.promotionEngine = promotionEngine;
    }

    public void reset() {
        this.order = new Order();
    }

    public void scan(String input) {
        parse(input).forEach(this::addItem);
    }

    private void addItem(String barcodeString) {
        Barcode barcode = new Barcode(barcodeString);
        Product product = catalog.findByCode(barcode.getProductCode());
        order.addItem(product, barcode.getQuantity());
    }

    private List<String> parse(String input) {
        return GSON.<List<String>>fromJson(input, List.class);
    }

    public void calculate() {
        order.calculate();
        promotionEngine.apply(order);
        order.calculateTotalPrice();
        order.calculateTotalSaving();
    }

    public String print() {
        Printable printer = createReceiptPrinter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        printer.print(new PrintStream(out));
        return out.toString();
    }

    private Printable createReceiptPrinter() {
        return new ReceiptPrinterBuilder(order).printHeader("***<没钱赚商店>购物清单***")
                .printItems()
                .printPromotionSummary()
                .printOrderSummary()
                .printFooter("**********")
                .build();
    }


}
