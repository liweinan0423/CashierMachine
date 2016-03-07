package machine;

public class PercentagePromotion extends Promotion {
    private final double percentage;

    public PercentagePromotion(double percentage, String[] barcodes) {
        super(barcodes);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
