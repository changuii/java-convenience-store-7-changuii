package store.domain;


public class Promotion {
    private final String promotionName;
    private final int purchaseCount;
    private final DateRange dateRange;

    private Promotion(final String promotionName, final int purchaseCount, final DateRange dateRange) {
        this.promotionName = promotionName;
        this.purchaseCount = purchaseCount;
        this.dateRange = dateRange;
    }

    public static Promotion of(final String promotionName, final int purchaseCount, final DateRange dateRange) {
        return new Promotion(promotionName, purchaseCount, dateRange);
    }

    public String getPromotionName() {
        return promotionName;
    }

    @Override
    public String toString() {
        return promotionName;
    }
}
