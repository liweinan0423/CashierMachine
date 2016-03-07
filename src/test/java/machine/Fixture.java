package machine;

import java.util.HashMap;
import java.util.Map;

public class Fixture {
    public static Map<String, Item> catalog;

    static {
        catalog = new HashMap<>();
        catalog.put("ITEM001", new Item(new Product("ITEM001", "可口可乐", 3.00, "瓶")));
        catalog.put("ITEM002", new Item(new Product("ITEM002", "羽毛球", 2.00, "个")));
        catalog.put("ITEM003", new Item(new Product("ITEM003", "苹果", 3.00, "斤")));
    }
}
