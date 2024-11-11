package store.service;

import java.util.List;
import store.component.generator.ConsumerGenerator;
import store.component.generator.LocalDateGenerator;
import store.domain.Bill;
import store.domain.Consumer;
import store.domain.ProductInventory;
import store.domain.PurchaseProduct;


public class ConvenienceStoreService {
    private static final double MEMBERSHIP_DISCOUNT_PERCENTAGE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT_PRICE = 8_000;
    private final ProductInventory productInventory;
    private final LocalDateGenerator localDateGenerator;
    private final ConsumerGenerator consumerGenerator;

    private ConvenienceStoreService(final ProductInventory productInventory,
                                    final LocalDateGenerator localDateGenerator) {
        this.productInventory = productInventory;
        this.localDateGenerator = localDateGenerator;
        this.consumerGenerator = new ConsumerGenerator(productInventory, localDateGenerator);
    }

    public static ConvenienceStoreService of(final ProductInventory productInventory,
                                             final LocalDateGenerator localDateGenerator) {
        return new ConvenienceStoreService(productInventory, localDateGenerator);
    }

    public boolean isPromotionInProgress(final Consumer consumer) {
        return consumer.isCurrentProductPromotionInProgress(productInventory, localDateGenerator.generate());
    }

    public boolean isInventoryQuantityRequirementMetForApplyPromotion(final Consumer consumer) {
        return consumer.isInventoryQuantityRequirementMetForApplyPromotion(productInventory);
    }

    public int getQuantityAtRegularPrice(final Consumer consumer) {
        return consumer.calculateCurrentProductQuantityAtRegularPrice(productInventory);
    }

    public void purchasePromotionProduct(final Consumer consumer) {
        consumer.purchasePromotionProduct(productInventory);
    }

    public void purchaseRegularPricePromotionProduct(final Consumer consumer) {
        consumer.purchaseRegularPricePromotionProduct(productInventory);
    }

    public boolean isQuantitySufficientForApplyPromotion(final Consumer consumer) {
        return consumer.isCurrentProductQuantitySufficientForApplyPromotion(productInventory);
    }

    public void purchaseProduct(final Consumer consumer) {
        consumer.purchaseProduct(productInventory);
    }

    public void discountMembership(final Bill bill) {
        bill.discountMembership(MEMBERSHIP_DISCOUNT_PERCENTAGE, MAX_MEMBERSHIP_DISCOUNT_PRICE);
    }

    public Consumer generateConsumer(final List<PurchaseProduct> purchaseProducts) {
        return consumerGenerator.generate(purchaseProducts);
    }

    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
