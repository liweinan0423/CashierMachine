package machine;

public class NOPPromotion extends Promotion {
    public NOPPromotion(String... barcodes) {
        super(barcodes);
    }

    @Override
    public void apply(Item item) {
    }
}
