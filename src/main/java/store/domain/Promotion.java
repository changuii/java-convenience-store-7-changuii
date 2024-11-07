package store.domain;

import java.util.Map;
import java.util.Optional;

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

    public void registerPromotion(final Map<String, Optional<Promotion>> promotions) {
        promotions.put(promotionName, Optional.of(this));
    }

    @Override
    public String toString() {
        return promotionName;
    }
}
