package machine.promotion;

import com.google.gson.Gson;
import machine.order.Item;
import machine.order.Order;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromotionEngine {
    private List<Promotion> promotions = new ArrayList<>();

    public PromotionEngine() {
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    private void apply(Item item) {
        getPromotions().forEach(promotion -> {
            if (promotion.supports(item)) {
                promotion.apply(item);
            }
        });
    }

    public void apply(Order order) {
        order.getItems().forEach(this::apply);
    }

    public void loadPromotion(String resource) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resource)) {
            parseJSON(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load promotion setting file", e);
        }

    }

    private void parseJSON(InputStream inputStream) {
        List<Map> list = new Gson().<List<Map>>fromJson(new InputStreamReader(inputStream), List.class);
        this.promotions = list.stream().map(this::createPromotion).collect(Collectors.toList());
    }

    private Promotion createPromotion(Map map) {
        String _type = (String) map.get("_type");
        switch (_type) {
            case "percentage":
                return createPercentagePromotion(map);
            case "buy_get":
                return createBuyGetPromotion(map);
            case "composite":
                return createCompositePromotion(map);
            default:
                throw new IllegalArgumentException("unknown promotion type");
        }
    }

    private Promotion createCompositePromotion(Map map) {
        Promotion highPriority = createPromotion((Map) map.get("highPriority"));
        Promotion lowPriority = createPromotion((Map) map.get("lowPriority"));
        return new CompositePromotion(highPriority, lowPriority);
    }

    private Promotion createBuyGetPromotion(Map map) {
        return new BuyXGetYFreePromotion(
                Integer.valueOf(map.get("x").toString()),
                Integer.valueOf(map.get("y").toString()),
                (List) map.get("productCodes"));
    }

    private PercentagePromotion createPercentagePromotion(Map map) {
        return new PercentagePromotion(Double.valueOf(map.get("percentage").toString()), (List) map.get("productCodes"));
    }
}
