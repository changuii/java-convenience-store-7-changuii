package store.handler;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class RetryHandler {

    public <T> void retryUntilFalse(Consumer<T> logic, BooleanSupplier flag, T data) {
        while (true) {
            logic.accept(data);
            if (flag.getAsBoolean()) {
                break;
            }
        }
    }
}
