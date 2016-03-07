package machine;

import java.util.Arrays;
import java.util.List;

public class Promotion {
    protected final List<String> barcodes;

    public Promotion(String[] barcodes) {
        this.barcodes = Arrays.asList(barcodes);
    }

    public List<String> getBarcodes() {
        return barcodes;
    }
}
