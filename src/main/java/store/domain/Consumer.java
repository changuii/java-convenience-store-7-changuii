package store.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Consumer {
    private final List<PurchaseProduct> purchaseProducts;
    private final List<CompletedPurchaseHistory> purchaseHistories;

    private Consumer(final List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
        this.purchaseHistories = new ArrayList<>();
    }

    public static Consumer from(final List<PurchaseProduct> purchaseProducts) {
        return new Consumer(purchaseProducts);
    }

    public boolean isPromotionProduct(final ProductInventory productInventory, final LocalDate today) {
        return productInventory.isPromotionProduct(currentPurchaseProduct(), today);
    }

    public boolean isPromotionProductQuantityEnogh(final ProductInventory productInventory) {
        return productInventory.isPromotionQuantityEnough(currentPurchaseProduct());
    }

    public boolean isQuantityPromotionSufficient(final ProductInventory productInventory) {
        return productInventory.isQuantityPromotionSufficient(currentPurchaseProduct());
    }

    public void addtionProductQuantity() {
        currentPurchaseProduct().additionQuantity();
    }

    public int calculateQuantityAtRegularPrice(final ProductInventory productInventory) {
        return productInventory.calculateQuantityAtRegularPrice(currentPurchaseProduct());
    }

    public void purchaseProduct(final ProductInventory productInventory) {
        productInventory.purchaseProduct(currentPurchaseProduct());
        addIfCurrentPurchaseProductComplete();
    }

    public void deductCurrentProductQuantityAtRegularPrice(final int regularPriceQuantity) {
        currentPurchaseProduct().deductQuantity(regularPriceQuantity);
    }

    public void purchasePromotionProduct(final ProductInventory productInventory) {
        productInventory.purchasePromotionProduct(currentPurchaseProduct());
        productInventory.purchaseRegularPricePromotionProduct(currentPurchaseProduct());
        productInventory.purchaseProduct(currentPurchaseProduct());
        addIfCurrentPurchaseProductComplete();
    }

    public void purchaseRegularPricePromotionProduct(final ProductInventory productInventory) {
        productInventory.purchaseRegularPricePromotionProduct(currentPurchaseProduct());
        addIfCurrentPurchaseProductComplete();
    }

    public void addIfCurrentPurchaseProductComplete() {
        if (currentPurchaseProduct().isPurchaseCompleted()) {
            purchaseHistories.add(currentPurchaseProduct().generateCompletedPurchaseHistory());
        }
    }

    public boolean isCurrentPurchaseProductDone() {
        return currentPurchaseProduct().isPurchaseCompleted();
    }

    public void nextPurchaseProduct() {
        removePurchaseCompletedProduct();
    }

    public String currentProductName() {
        return currentPurchaseProduct().getName();
    }

    private PurchaseProduct currentPurchaseProduct() {
        return purchaseProducts.getFirst();
    }

    private void removePurchaseCompletedProduct() {
        purchaseProducts.removeFirst();
    }

    public boolean isCheckoutCompleted() {
        return purchaseProducts.isEmpty();
    }

}
