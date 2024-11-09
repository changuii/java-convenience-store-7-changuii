package store.service;

import java.util.List;
import store.component.ConsumerGenerator;
import store.component.LocalDateGenerator;
import store.domain.Consumer;
import store.domain.ProductInventory;
import store.domain.PurchaseHistory;
import store.domain.PurchaseProduct;
import store.dto.PurchaseProductDTO;


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

    public Consumer generateConsumer(List<PurchaseProduct> purchaseProducts) {
        return consumerGenerator.generate(purchaseProducts);
    }

    public ProductInventory getProductInventory() {
        return productInventory;
    }

}
