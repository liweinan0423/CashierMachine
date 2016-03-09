package machine.product;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class LocalProductCatalog implements ProductCatalog {

    private Map<String, Product> products;

    public LocalProductCatalog(String resource) {
        try (InputStream inputStream = getClass().getResourceAsStream(resource)) {
            this.products = map(parseJSON(inputStream));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load product file", e);
        }
    }

    private List<Product> parseJSON(InputStream inputStream) {
        return new Gson().<List<Product>>fromJson(new InputStreamReader(inputStream), ProductList.class);
    }

    private Map<String, Product> map(List<Product> products) {
        return products.stream().collect(Collectors.toMap(Product::getProductCode, identity()));
    }

    @Override
    public Product findByCode(String code) {
        return products.get(code);
    }

    private static class ProductList extends ArrayList<Product> {
    }
}
