package store.domain;

public class PurchaseProduct {
    private final String name;
    private int currentQuantity;

    private PurchaseProduct(final String name, final int currentQuantity) {
        this.name = name;
        this.currentQuantity = currentQuantity;
    }

    public static PurchaseProduct of(final String name, final int currentQuantity) {
        return new PurchaseProduct(name, currentQuantity);
    }

    public boolean isMatchProductName(final String productName) {
        return name.equals(productName);
    }

    public boolean isLessThanQuantity(final PromotionProductQuantity promotionQuantity,
                                      final ProductQuantity quantity) {
        return promotionQuantity.isLessThanQuantity(currentQuantity, quantity);
    }

    public boolean isLessThanQuantity(final ProductQuantity quantity){
        return quantity.isLessThanQuantity(this.currentQuantity);
    }
}
