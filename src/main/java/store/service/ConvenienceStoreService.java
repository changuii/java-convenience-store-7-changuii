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

    public boolean isPromotionInProgress(Consumer consumer){
        return consumer.isPromotionProduct(productInventory, localDateGenerator.generate());
    }

    public boolean isPromotionProductEnogh(Consumer consumer){
        return consumer.isPromotionProductQuantityEnogh(productInventory);
    }

    public int getQuantityAtRegularPrice(Consumer consumer){
        return consumer.calculateQuantityAtRegularPrice(productInventory);
    }

    public void purchaseProduct(Consumer consumer){
        consumer.purchaseProduct(productInventory);
    }


    public Consumer generateConsumer(List<PurchaseProduct> purchaseProducts) {
        return consumerGenerator.generate(purchaseProducts);
    }

    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
