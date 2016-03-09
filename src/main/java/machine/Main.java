package machine;

import machine.machine.CashierMachine;
import machine.product.LocalProductCatalog;
import machine.promotion.PromotionEngine;

import static java.lang.System.exit;

public class Main {

    private static CashierMachine machine;

    public static void main(String[] args) {
        checkArgs(args);
        setup();
        run(args[0]);
        print(machine.print());
    }

    private static void checkArgs(String[] args) {
        if (args.length != 1) {
            printUsage();
            exit(1);
        }
    }

    private static void setup() {
        PromotionEngine promotionEngine = new PromotionEngine();
        promotionEngine.loadPromotion("/promotions.json");
        machine = new CashierMachine(new LocalProductCatalog("/products.json"), promotionEngine);
    }

    private static void run(String arg) {
        machine.start();
        machine.scan(arg);
        machine.calculate();
    }

    private static void print(String print) {
        System.out.println(print);
    }

    private static void printUsage() {
        print("usage: java machine.Main ['ITEM001','ITEM002']");
    }
}
