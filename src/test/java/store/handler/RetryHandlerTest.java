package store.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.view.OutputView;

public class RetryHandlerTest {

    private final RetryHandler retryHandler;
    private final MockOutputView mockOutputView;


    public RetryHandlerTest() {
        retryHandler = new RetryHandler();
        mockOutputView = new MockOutputView();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 20, 50, 100})
    void 예외가_발생하지_않을때_까지_반복_실행한다(int N) {
        Counter counter = new Counter(N);

        int actual = retryHandler.retryUntilNotException(counter::countFromUntilN, mockOutputView);

        assertThat(actual).isEqualTo(N);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 20, 30, 100})
    void False가_발생하지_않을때_까지_반복_실행한다(int N) {
        Counter counter = new Counter(N);

        retryHandler.retryUntilTrue(counter::countFromUntilN, counter::isValid, N);

        assertThat(counter.getCount()).isEqualTo(N);
    }


    private class Counter {
        private int count;
        private int N;

        public Counter(int N) {
            this.count = 0;
            this.N = N;
        }

        public int countFromUntilN() {
            count++;
            if (count < N) {
                throw new IllegalArgumentException();
            }
            return count;
        }

        public void countFromUntilN(int garbage) {
            count++;
        }

        public boolean isValid() {
            return count == N;
        }

        public int getCount() {
            return count;
        }


    }


    private class MockOutputView extends OutputView {

        @Override
        public void printErrorMessage(IllegalArgumentException e) {
            System.out.println("예외 발생");
        }
    }


}
