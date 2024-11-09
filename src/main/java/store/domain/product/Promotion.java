package store.domain.product;


import java.time.LocalDate;

public class Promotion {
    private final String promotionName;
    private final BuyGet buyGet;
    private final DateRange dateRange;

    private Promotion(final String promotionName, final BuyGet buyGet, final DateRange dateRange) {
        this.promotionName = promotionName;
        this.buyGet = buyGet;
        this.dateRange = dateRange;
    }

    public static Promotion of(final String promotionName, final BuyGet buyGet, final DateRange dateRange) {
        return new Promotion(promotionName, buyGet, dateRange);
    }

    public boolean isValidNow(final LocalDate today) {
        return dateRange.isInRangeNow(today);
    }

    public String getPromotionName() {
        return promotionName;
    }

    @Override
    public String toString() {
        return promotionName;
    }
}
