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

    // currentQuantity가 받아야할 무료 수량을 계산
    private int calculateRequiredFreeQuantityForApplyPromotion(final int currentQuantity) {
        return (currentQuantity + 1) / (buy + get);
    }

    // currentQuantity가 프로모션 혜택을 받으면서 구매할 수 있는 수량
    public int calculateRequiredQuantityForApplyPromotion(final int currentQuantity) {
        int freeQuantity = calculateRequiredFreeQuantityForApplyPromotion(currentQuantity);
        return freeQuantity + (freeQuantity * buy);
    }

    // 재고 수량의 적용가능한 무료 수량을 계산한다.
    public int calculateApplicableFreeQuantity(final int quantity) {
        return quantity / (buy + get);
    }

    // 재고 수량이 처리할 수 있는 최대 프로모션 수량을 계산한다.
    public int calculateApplicableQuantity(final int quantity) {
        return calculateApplicableFreeQuantity(quantity) * (buy + get);
    }


}
