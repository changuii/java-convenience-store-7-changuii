package store.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Consumer {
    private final List<PurchaseProduct> purchaseProducts;
    private final List<PurchaseHistory> purchaseHistories;

    private Consumer(final List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
        this.purchaseHistories = new ArrayList<>();
    }

    public static Consumer from(final List<PurchaseProduct> purchaseProducts) {
        return new Consumer(purchaseProducts);
    }

    public boolean isPromotionProduct(final ProductInventory productInventory, final LocalDate today){
        return productInventory.isPromotionProduct(currentPurchaseProduct(), today);
    }

    public void purchaseProduct(final ProductInventory productInventory){
        PurchaseHistory purchaseHistory = productInventory.purchaseProduct(currentPurchaseProduct());
        purchaseHistories.add(purchaseHistory);
        removePurchaseCompletedProduct();
    }


    private PurchaseProduct currentPurchaseProduct(){
        return purchaseProducts.getFirst();
    }

    private void removePurchaseCompletedProduct(){
        purchaseProducts.removeFirst();
    }

    public boolean isCheckoutCompleted() {
        return purchaseProducts.isEmpty();
    }

}
