package machine;

import machine.promotion.BuyXGetYFreePromotion;
import machine.promotion.CompositePromotion;
import machine.promotion.PercentagePromotion;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineTests {

    private CashierMachine machine;

    @Before
    public void setUp() {
        machine = new CashierMachine(new LocalProductCatalog("/fixture.json"));
    }

    @Test
    public void one_item() {
        machine.start();
        machine.scan("[ITEM001]");
        machine.calculate();
        String receipt = machine.print();

        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 1瓶, 单价: 3.00(元), 小计: 3.00(元)\n"
                + "----------\n"
                + "总计: 3.00(元)\n"
                + "**********\n";

        assertEquals(expected, receipt);
    }

    @Test
    public void one_item_multiple() {
        machine.start();
        machine.scan("['ITEM001', 'ITEM001']");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 2瓶, 单价: 3.00(元), 小计: 6.00(元)\n"
                + "----------\n"
                + "总计: 6.00(元)\n"
                + "**********\n";

        assertEquals(expected, receipt);
    }

    @Test
    public void multiple_items() {
        machine.start();
        machine.scan("['ITEM001', 'ITEM002', 'ITEM002']");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 1瓶, 单价: 3.00(元), 小计: 3.00(元)\n"
                + "名称: 羽毛球, 数量: 2个, 单价: 2.00(元), 小计: 4.00(元)\n"
                + "----------\n"
                + "总计: 7.00(元)\n"
                + "**********\n";
        assertEquals(expected, receipt);
    }

    @Test
    public void item_with_quantity() {
        machine.start();
        machine.scan("['ITEM003-2']");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 苹果, 数量: 2斤, 单价: 3.00(元), 小计: 6.00(元)\n"
                + "----------\n"
                + "总计: 6.00(元)\n"
                + "**********\n";
        assertEquals(expected, receipt);
    }

    @Test
    public void same_item_with_quantity_multiple_times() {
        machine.start();
        machine.scan("['ITEM003-2', 'ITEM003-2']");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 苹果, 数量: 4斤, 单价: 3.00(元), 小计: 12.00(元)\n"
                + "----------\n"
                + "总计: 12.00(元)\n"
                + "**********\n";
        assertEquals(expected, receipt);
    }

    @Test
    public void item_with_percentage_promotion() {
        machine.addPromotion(new PercentagePromotion(0.95, "ITEM001"));
        machine.start();
        machine.scan("[ITEM001]");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 1瓶, 单价: 3.00(元), 小计: 2.85(元), 节省0.15(元)\n"
                + "----------\n"
                + "总计: 2.85(元)\n"
                + "节省: 0.15(元)\n"
                + "**********\n";

        assertEquals(expected, receipt);
    }

    @Test
    public void item_with_buy_x_get_y_free_promotion() {
        machine.addPromotion(new BuyXGetYFreePromotion(2, 1, "ITEM001"));
        machine.start();
        machine.scan("[ITEM001,ITEM001,ITEM001]");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 4瓶, 单价: 3.00(元), 小计: 9.00(元)\n"
                + "----------\n"
                + "买二赠一商品:\n"
                + "名称: 可口可乐, 数量: 1瓶\n"
                + "----------\n"
                + "总计: 9.00(元)\n"
                + "节省: 3.00(元)\n"
                + "**********\n";
        assertEquals(expected, receipt);

    }

    @Test
    public void buy_get_promotion_should_supersede_percentage_promotion() {
        PercentagePromotion percentagePromotion = new PercentagePromotion(0.95, "ITEM001");
        BuyXGetYFreePromotion buy2Get1FreePromotion = new BuyXGetYFreePromotion(2, 1, "ITEM001");
        CompositePromotion compositePromotion = new CompositePromotion(buy2Get1FreePromotion, percentagePromotion);
        machine.addPromotion(compositePromotion);
        machine.start();
        machine.scan("[ITEM001, ITEM001]");
        machine.calculate();
        String receipt = machine.print();
        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 3瓶, 单价: 3.00(元), 小计: 6.00(元)\n"
                + "----------\n"
                + "买二赠一商品:\n"
                + "名称: 可口可乐, 数量: 1瓶\n"
                + "----------\n"
                + "总计: 6.00(元)\n"
                + "节省: 3.00(元)\n"
                + "**********\n";
        assertEquals(expected, receipt);
    }

}
