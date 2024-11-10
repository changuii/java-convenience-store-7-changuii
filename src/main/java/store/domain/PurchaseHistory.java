package store.domain;

public class PurchaseHistory {
    private final String productName;
    private int totalPurchasePrice;
    private int quantity;
    private int promotionQuantity;
    private int freeQuantity;

    private PurchaseHistory(final String productName) {
        this.productName = productName;
        this.totalPurchasePrice = 0;
        this.quantity = 0;
        this.promotionQuantity = 0;
        this.freeQuantity = 0;
    }

    public static PurchaseHistory from(final String productName) {
        return new PurchaseHistory(productName);
    }

    public void addPurchasePrice(final int price){
        this.totalPurchasePrice += price;
    }

    public void addQuantity(final int quantity){
        this.quantity += quantity;
    }

    public void addPromotionQuantity(final int promotionQuantity){
        this.promotionQuantity += promotionQuantity;
    }

    public void addFreeQuantity(final int freeQuantity){
        this.freeQuantity = freeQuantity;
    }

    public CompletedPurchaseHistory purchaseComplete(){
        return CompletedPurchaseHistory.of(productName, totalPurchasePrice, quantity, promotionQuantity, freeQuantity);
    }



}
