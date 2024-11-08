package store.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import store.component.ConvenienceStoreGnerator;
import store.domain.ConvenienceStore;
import store.domain.ProductInfo;
import store.domain.ProductInventory;
import store.domain.ProductQuantity;
import store.domain.PromotionProductQuantity;
import store.dto.ProductInfoDTO;
import store.dto.ProductInventoryDTO;
import store.dto.ProductQuantityDTO;
import store.dto.PromotionProductQuantityDTO;
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
        String answer = retryHandler.retryUntilNotException(inputView::readAnswer, outputView);
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
        outputView.printStoreIntroduce(convertProductInventoryDTO(convenienceStore.getProductInventory()));
    }

    private ProductInventoryDTO convertProductInventoryDTO(ProductInventory productInventory) {
        return ProductInventoryDTO.from(
                convertProductInfoDTOs(productInventory.getInfos()),
                convertProductQuantitiesDTOs(productInventory.getQuantities()),
                convertPromotionProductQuantitiesDTOs(productInventory.getPromotionQuantities())
        );
    }

    private Map<String, ProductInfoDTO> convertProductInfoDTOs(Map<String, ProductInfo> infos) {
        Map<String, ProductInfoDTO> productInfoDTOs = new LinkedHashMap<>();

        infos.keySet().stream()
                .forEach(productName -> {
                    productInfoDTOs.put(productName, convertProductInfoDTO(infos.get(productName)));
                });
        return productInfoDTOs;
    }

    private Map<String, ProductQuantityDTO> convertProductQuantitiesDTOs(Map<String, ProductQuantity> quantities) {
        Map<String, ProductQuantityDTO> productQuantityDTOs = new LinkedHashMap<>();

        quantities.keySet().stream()
                .forEach(productName -> {
                    productQuantityDTOs.put(productName, convertProductQuantityDTO(quantities.get(productName)));
                });
        return productQuantityDTOs;
    }

    private Map<String, PromotionProductQuantityDTO> convertPromotionProductQuantitiesDTOs(
            Map<String, PromotionProductQuantity> promotionQuantities) {
        Map<String, PromotionProductQuantityDTO> promotionQuantityDTOs = new LinkedHashMap<>();

        promotionQuantities.keySet().stream()
                .forEach(productName -> {
                    promotionQuantityDTOs.put(productName,
                            convertPromotionProductQuantityDTO(promotionQuantities.get(productName)));
                });
        return promotionQuantityDTOs;
    }

    private ProductInfoDTO convertProductInfoDTO(ProductInfo productInfo) {
        return ProductInfoDTO.of(productInfo.getName(), productInfo.getPrice());
    }

    private ProductQuantityDTO convertProductQuantityDTO(ProductQuantity productQuantity) {
        return ProductQuantityDTO.from(productQuantity.getQuantity());
    }

    private PromotionProductQuantityDTO convertPromotionProductQuantityDTO(PromotionProductQuantity promotionQuantity) {
        return PromotionProductQuantityDTO.of(promotionQuantity.getQuantity(), promotionQuantity.getPromotion());
    }


}
