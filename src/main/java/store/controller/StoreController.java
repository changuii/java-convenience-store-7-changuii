package store.controller;

import java.util.List;
import store.component.DTOConverter;
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
        retryHandler.retryUntilFalse(this::runConvenienceStoreCheckout, this::isTerminationCheckout,
                convenienceStoreService);
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

    private void runConvenienceStoreCheckout(final ConvenienceStoreService convenienceStoreService) {
        printConvenienceStore(convenienceStoreService);
        outputView.printPurchaseProductsInputMessage();
        List<PurchaseProductDTO> purchaseProductDTOs = retryHandler.retryUntilNotException(
                this::requestPurchaseProductDTOs, outputView);

    }


    private void printConvenienceStore(final ConvenienceStoreService convenienceStoreService) {
        outputView.printWelcomeMessage();
        outputView.printStoreIntroduce(
                dtoConverter.convertProductInventoryDTO(convenienceStoreService.getProductInventory()));
    }

    private List<PurchaseProductDTO> requestPurchaseProductDTOs() {
        List<PurchaseProductDTO> purchaseProductDTOs = inputView.readPurchaseProducts();
        convenienceStoreService.validatePurchaseProducts(purchaseProductDTOs);
        return purchaseProductDTOs;
    }


}
