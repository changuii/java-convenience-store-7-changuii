package store.domain.product;

public class BuyGet {
    private final int buy;
    private final int get;

    private BuyGet(final int buy, final int get) {
        this.buy = buy;
        this.get = get;
    }

    public static BuyGet of(final int buy, final int get) {
        return new BuyGet(buy, get);
    }

    private int calculateRequiredFreeQuantityForApplyPromotion(final int currentQuantity) {
        return (currentQuantity + 1) / (buy + get);
    }

    public int calculateRequiredQuantityForApplyPromotion(final int currentQuantity) {
        int freeQuantity = calculateRequiredFreeQuantityForApplyPromotion(currentQuantity);
        return freeQuantity + (freeQuantity * buy);
    }

    public int calculateApplicableFreeQuantity(final int quantity) {
        return quantity / (buy + get);
    }

    public int calculateApplicableQuantity(final int quantity) {
        return calculateApplicableFreeQuantity(quantity) * (buy + get);
    }


}
