package store;

import store.controller.StoreController;
import store.factory.StoreControllerFactory;

public class Application {
    public static void main(String[] args) {
        StoreController storeController = StoreControllerFactory.create();
        storeController.run();
    }
}
