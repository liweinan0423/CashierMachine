package com.thoughtworks.cashiermachine;

import com.thoughtworks.cashiermachine.app.CashierMachine;
import com.thoughtworks.cashiermachine.promotion.PromotionEngine;
import com.thoughtworks.cashiermachine.product.LocalProductCatalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class Main {

    private static CashierMachine machine;

    public static void main(String[] args) throws IOException {
        setup();
        run();
    }

    private static void setup() {
        PromotionEngine promotionEngine = new PromotionEngine();
        promotionEngine.loadPromotion("/promotions.json");
        machine = new CashierMachine(new LocalProductCatalog("/products.json"), promotionEngine);
    }

    private static void run() throws IOException {
        while (true) {
            machine.reset();
            prompt();
            String line = scan();
            if (userTypesExit(line)) {
                exit(0);
            } else {
                machine.scan(line);
                machine.calculate();
                print(machine.print());
            }
        }
    }

    private static boolean userTypesExit(String line) {
        return "exit".equalsIgnoreCase(line);
    }

    private static void prompt() {
        print("Please type barcodes, format is like ['ITEM001','ITEM002',...]");
        print("Type 'exit' to close the machine...");
    }

    private static String scan() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    private static void print(String print) {
        System.out.println(print);
    }
}
