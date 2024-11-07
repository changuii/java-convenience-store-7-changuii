package store.controller;

import store.component.ConvenienceStoreGnerator;
import store.domain.ConvenienceStore;
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
        outputView.printStoreIntroduce(convenienceStore.toDTO());
    }

}
