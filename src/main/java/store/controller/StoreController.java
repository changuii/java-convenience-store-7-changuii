package store.controller;

import java.util.List;
import store.component.DTOConverter;
import store.domain.Consumer;
import store.domain.PurchaseProduct;
import store.service.ConvenienceStoreService;
import store.dto.PurchaseProductDTO;
import store.handler.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceStoreService convenienceStoreService;
    private final RetryHandler retryHandler;
    private final DTOConverter dtoConverter;

    public StoreController(final InputView inputView, final OutputView outputView,
                           final ConvenienceStoreService convenienceStoreService, final RetryHandler retryHandler,
                           final DTOConverter dtoConverter) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceStoreService = convenienceStoreService;
        this.retryHandler = retryHandler;
        this.dtoConverter = dtoConverter;

    }

    public void run() {
        retryHandler.retryUntilTrue(this::runConvenienceStoreCheckout, this::isCheckoutCompleted,
                convenienceStoreService);
    }

    private void runConvenienceStoreCheckout(final ConvenienceStoreService convenienceStoreService) {
        printConvenienceStore(convenienceStoreService);
        outputView.printPurchaseProductsInputMessage();
        Consumer consumer = retryHandler.retryUntilNotException(this::requestConsumer, outputView);
        retryHandler.retryUntilTrue(this::checkoutConsumer, consumer::isCheckoutCompleted, consumer);
    }

    private boolean isCheckoutCompleted() {
        outputView.printRequestContinueCheckoutMessage();
        String answer = retryHandler.retryUntilNotException(inputView::readAnswer, outputView);
        outputView.printLineBreak();
        return answer.equals("N");
    }


    private void printConvenienceStore(final ConvenienceStoreService convenienceStoreService) {
        outputView.printWelcomeMessage();
        outputView.printStoreIntroduce(
                dtoConverter.convertProductInventoryDTO(convenienceStoreService.getProductInventory()));
    }

    private Consumer requestConsumer() {
        List<PurchaseProductDTO> purchaseProductDTOs = inputView.readPurchaseProducts();
        List<PurchaseProduct> purchaseProducts = dtoConverter.convertPurchaseProducts(purchaseProductDTOs);
        return convenienceStoreService.generateConsumer(purchaseProducts);
    }

    private void checkoutConsumer(Consumer consumer) {
        if (!convenienceStoreService.isPromotionInProgress(consumer)) {
            convenienceStoreService.purchaseProduct(consumer);
        }


    }


}
