package machine;

import java.util.Arrays;
import java.util.List;

public class BuyXGetYFreePromotion {
    private final int x;
    private final int y;
    private final List<String> barcodes;

    public BuyXGetYFreePromotion(int x, int y, String... barcodes) {
        this.x = x;
        this.y = y;
        this.barcodes = Arrays.asList(barcodes);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<String> getBarcodes() {
        return barcodes;
    }
}
