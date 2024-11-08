package store.controller;

import store.component.ConvenienceStoreGnerator;
import store.component.DTOConverter;
import store.domain.ConvenienceStore;
import store.domain.ProductInventory;
import store.dto.ProductInventoryDTO;
import store.handler.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceStoreGnerator convenienceStoreGnerator;
    private final RetryHandler retryHandler;
    private final DTOConverter dtoConverter;

    public StoreController(final InputView inputView, final OutputView outputView,
                           final ConvenienceStoreGnerator ConvenienceStoreGnerator, final RetryHandler retryHandler,
                           final DTOConverter dtoConverter) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceStoreGnerator = ConvenienceStoreGnerator;
        this.retryHandler = retryHandler;
        this.dtoConverter = dtoConverter;

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
        outputView.printStoreIntroduce(dtoConverter.convertProductInventoryDTO(convenienceStore.getProductInventory()));
    }


}
