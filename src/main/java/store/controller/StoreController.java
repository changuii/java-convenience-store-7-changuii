package store.controller;

import store.component.ConvenienceStoreGnerator;
import store.domain.ConvenienceStore;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final ConvenienceStoreGnerator convenienceStoreGnerator;

    public StoreController(final InputView inputView, final OutputView outputView,
                           final ConvenienceStoreGnerator ConvenienceStoreGnerator) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.convenienceStoreGnerator = ConvenienceStoreGnerator;

    }
    public void run() {
        ConvenienceStore convenienceStore = convenienceStoreGnerator.generate();
        printConvenienceStore(convenienceStore);
    }

    private void printConvenienceStore(ConvenienceStore convenienceStore){
        outputView.printWelcomeMessage();
        outputView.printStoreIntroduce(convenienceStore.toDTO());
    }

}
