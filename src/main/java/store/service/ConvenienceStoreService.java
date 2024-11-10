package store.service;

import java.util.List;
import store.component.generator.ConsumerGenerator;
import store.component.generator.LocalDateGenerator;
import store.domain.Consumer;
import store.domain.ProductInventory;
import store.domain.PurchaseProduct;


public class ConvenienceStoreService {
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
        return consumer.isPromotionProduct(productInventory, localDateGenerator.generate());
    }

    public boolean isPromotionProductEnogh(final Consumer consumer) {
        return consumer.isPromotionProductQuantityEnogh(productInventory);
    }

    public void purchaseRegularPricePromotionProduct(final Consumer consumer) {
        consumer.purchaseRegularPricePromotionProduct(productInventory);
    }

    public boolean isQuantityPromotionSufficient(final Consumer consumer) {
        return consumer.isQuantityPromotionSufficient(productInventory);
    }

    public int getQuantityAtRegularPrice(final Consumer consumer) {
        return consumer.calculateQuantityAtRegularPrice(productInventory);
    }

    public void purchasePromotionProduct(final Consumer consumer) {
        consumer.purchasePromotionProduct(productInventory);
    }

    public void purchaseProduct(final Consumer consumer) {
        consumer.purchaseProduct(productInventory);
    }


    public Consumer generateConsumer(final List<PurchaseProduct> purchaseProducts) {
        return consumerGenerator.generate(purchaseProducts);
    }

    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
