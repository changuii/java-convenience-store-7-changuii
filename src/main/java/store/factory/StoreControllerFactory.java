package store.factory;

import store.component.ConvenienceStoreGnerator;
import store.component.DTOConverter;
import store.component.FileParser;
import store.component.ProductInventoryGenerator;
import store.component.PromotionGenerator;
import store.controller.StoreController;
import store.handler.RetryHandler;
import store.service.ConvenienceStoreService;
import store.view.InputParser;
import store.view.InputValidator;
import store.view.InputView;
import store.view.OutputView;

public abstract class StoreControllerFactory {

    public static StoreController create() {
        return new StoreController(createInputView(), createOutputView(), createConvenienceService(),
                createRetryHandle(), createDTOConverter());
    }

    private static InputView createInputView() {
        return new InputView(new InputValidator(), new InputParser());
    }

    private static OutputView createOutputView() {
        return new OutputView();
    }

    private static ConvenienceStoreGnerator createConvenienceStoreGenerator() {
        return new ConvenienceStoreGnerator(new FileParser(), new PromotionGenerator(),
                new ProductInventoryGenerator());
    }

    private static ConvenienceStoreService createConvenienceService(){
        return createConvenienceStoreGenerator().generate();
    }

    private static RetryHandler createRetryHandle() {
        return new RetryHandler();
    }

    private static DTOConverter createDTOConverter() {
        return new DTOConverter();
    }
}
