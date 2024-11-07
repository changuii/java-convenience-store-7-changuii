package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import store.component.ConvenienceStoreGnerator;
import store.domain.ConvenienceStore;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ConvenienceStoreDTO;
import store.dto.ProductDTO;
import store.handler.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceStoreGnerator convenienceStoreGnerator;
    private final RetryHandler retryHandler;

    public StoreController(final InputView inputView, final OutputView outputView,
                           final ConvenienceStoreGnerator ConvenienceStoreGnerator, final RetryHandler retryHandler) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceStoreGnerator = ConvenienceStoreGnerator;
        this.retryHandler = retryHandler;

    }

    public void run() {
        ConvenienceStore convenienceStore = convenienceStoreGnerator.generate();
        retryHandler.retryUntilFalse(this::runConvenienceStoreCheckout, this::isTerminationCheckout, convenienceStore);
    }

    private boolean isTerminationCheckout() {
        outputView.printRequestContinueCheckoutMessage();
        String answer = inputView.readAnswer();
        outputView.printLineBreak();
        if (answer.equals("Y")) {
            return false;
        }
        return true;
    }

    private void runConvenienceStoreCheckout(ConvenienceStore convenienceStore) {
        printConvenienceStore(convenienceStore);
    }


    private void printConvenienceStore(ConvenienceStore convenienceStore) {
        outputView.printWelcomeMessage();
        outputView.printStoreIntroduce(convertConvenienceDTO(convenienceStore));
    }

    private ConvenienceStoreDTO convertConvenienceDTO(ConvenienceStore convenienceStore) {
        Map<String, Product> products = convenienceStore.getProducts();
        Map<String, Product> promotionProducts = convenienceStore.getPromotionProducts();
        List<ProductDTO> productInventory = new ArrayList<>();

        products.keySet().forEach(productName ->
                registerProduct(productName, products, promotionProducts, productInventory));
        return ConvenienceStoreDTO.from(productInventory);
    }

    private void registerProduct(String productName, final Map<String, Product> products,
                                 final Map<String, Product> promotionProducts,
                                 final List<ProductDTO> productInventory) {
        if (promotionProducts.containsKey(productName)) {
            productInventory.add(convertProductDTO(promotionProducts.get(productName)));
        }
        productInventory.add(convertProductDTO(products.get(productName)));
    }

    private ProductDTO convertProductDTO(final Product product) {
        return ProductDTO.of(product.getName(), product.getPrice(), product.getQuantity(),
                convertPromotion(product.getPromotion()));
    }

    private String convertPromotion(final Optional<Promotion> promotion) {
        if (promotion.isPresent()) {
            return promotion.get().getPromotionName();
        }
        return "";
    }

}
