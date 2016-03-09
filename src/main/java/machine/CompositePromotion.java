package machine;

public class CompositePromotion extends Promotion {

    private final Promotion highPriority;
    private final Promotion lowPriority;

    public CompositePromotion(Promotion highPriority, Promotion lowPriority) {
        this.highPriority = highPriority;
        this.lowPriority = lowPriority;
    }


    @Override
    public boolean supports(Item item) {
        return highPriority.supports(item) || lowPriority.supports(item);
    }

    @Override
    public void apply(Item item) {
        if (highPriority.supports(item)) {
            highPriority.apply(item);
        } else if (lowPriority.supports(item)) {
            highPriority.apply(item);
        }
    }
}
