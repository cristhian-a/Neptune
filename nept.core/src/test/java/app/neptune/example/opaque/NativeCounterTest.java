package app.neptune.example.opaque;

import org.junit.jupiter.api.Test;

final class NativeCounterTest {

    @Test
    void lifecycleTest() {
        try (var counter = NativeCounter.create(10)) {

            int val = counter.get();
            assert val == 10;
            IO.println("val: " + counter.get());

            counter.increment();
            int val2 = counter.get();
            assert val2 == 11;
            IO.println("val: " + counter.get());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
