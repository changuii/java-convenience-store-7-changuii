package store.factory;

import store.component.ConvenienceStoreGnerator;
import store.component.FileParser;
import store.component.ProductGenerator;
import store.component.PromotionGenerator;
import store.controller.StoreController;
import store.handler.RetryHandler;
import store.view.InputParser;
import store.view.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public abstract class StoreControllerFactory {

    public static StoreController create() {
        return new StoreController(createInputView(), createOutputView(), createConvenienceStoreGenerator(),
                createRetryHandle());
    }

    private static InputView createInputView() {
        return new InputView(new InputValidator(), new InputParser());
    }

    private static OutputView createOutputView() {
        return new OutputView();
    }

    private static ConvenienceStoreGnerator createConvenienceStoreGenerator() {
        return new ConvenienceStoreGnerator(new FileParser(), new PromotionGenerator(), new ProductGenerator());
    }

    private static RetryHandler createRetryHandle() {
        return new RetryHandler();
    }
}
