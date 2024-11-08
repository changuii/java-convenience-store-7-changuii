package store.handler;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import store.view.OutputView;

public class RetryHandler {

    public <T> void retryUntilFalse(Consumer<T> logic, BooleanSupplier flag, T data) {
        while (true) {
            logic.accept(data);
            if (flag.getAsBoolean()) {
                break;
            }
        }
    }

    public <T> T retryUntilNotException(Supplier<T> logic, OutputView outputView) {
        while (true) {
            try {
                return logic.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e);
            }
        }
    }
}
