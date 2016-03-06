package machine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MachineTests {

    @Test
    public void one_item() {
        CashierMachine machine = new CashierMachine("没钱赚商店");
        machine.start();
        machine.scan("[ITEM001]");
        machine.calculate();
        String receipt = machine.print();
        machine.reset();

        String expected = "***<没钱赚商店>购物清单***\n"
                + "名称: 可口可乐, 数量: 1瓶, 单价: 3.00(元), 小计: 3.00(元)\n"
                + "----------\n"
                + "总计: 3.00(元)\n"
                + "**********\n";

        assertEquals(expected, receipt);
    }

    @Test
    public void one_item_multiple() {
        CashierMachine machine = new CashierMachine("没钱赚商店");
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
}
