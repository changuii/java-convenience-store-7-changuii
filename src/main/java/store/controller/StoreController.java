package store.controller;

import store.component.ConvenienceStoreGnerator;
import store.domain.ConvenienceStore;
import store.view.InputView;

public class StoreController {

    private final InputView inputView;
    private final ConvenienceStoreGnerator convenienceStoreGnerator;

    public StoreController(final InputView inputView, final ConvenienceStoreGnerator ConvenienceStoreGnerator) {
        this.inputView = inputView;
        this.convenienceStoreGnerator = ConvenienceStoreGnerator;
    }


    public void run() {
        ConvenienceStore convenienceStore = convenienceStoreGnerator.generate();
    }

}
