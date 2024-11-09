package store.factory;

import store.component.generator.ConvenienceStoreServiceGnerator;
import store.component.DTOConverter;
import store.component.FileParser;
import store.component.generator.ProductInventoryGenerator;
import store.component.generator.PromotionGenerator;
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

    private static ConvenienceStoreServiceGnerator createConvenienceStoreGenerator() {
        return new ConvenienceStoreServiceGnerator(new FileParser(), new PromotionGenerator(),
                new ProductInventoryGenerator());
    }

    private static ConvenienceStoreService createConvenienceService() {
        return createConvenienceStoreGenerator().generate();
    }

    private static RetryHandler createRetryHandle() {
        return new RetryHandler();
    }

    private static DTOConverter createDTOConverter() {
        return new DTOConverter();
    }
}
