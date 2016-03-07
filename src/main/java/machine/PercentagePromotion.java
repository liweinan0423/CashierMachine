package machine;

import java.util.Arrays;
import java.util.List;

public class PercentagePromotion {
    private final double percentage;
    private final List<String> barcodes;

    public PercentagePromotion(double percentage, String[] barcodes) {
        this.percentage = percentage;
        this.barcodes = Arrays.asList(barcodes);
    }

    public double getPercentage() {
        return percentage;
    }

    public List<String> getBarcodes() {
        return barcodes;
    }
}
