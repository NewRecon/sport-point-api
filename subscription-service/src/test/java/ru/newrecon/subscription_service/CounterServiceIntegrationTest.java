package ru.newrecon.subscription_service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.newrecon.subscription_service.service.CounterService;

@SpringBootTest
public class CounterServiceIntegrationTest {

    private static final String COUNTER_KEY = "test:counter";

    @Autowired
    private CounterService counterService;

    @AfterEach
    void tearDown() {
        counterService.deleteCounter(COUNTER_KEY);
    }

    @Test
    void testCounterLifecycle() {
        long initialValue = counterService.getValue(COUNTER_KEY);
        Assertions.assertThat(initialValue).isEqualTo(0);
        
        long valueAfterSet = counterService.setCounterValue(COUNTER_KEY, 500);
        Assertions.assertThat(valueAfterSet).isEqualTo(500);

        long afterSetInc = counterService.increment(COUNTER_KEY);
        Assertions.assertThat(afterSetInc).isEqualTo(501);

        long afterSetDec= counterService.decrement(COUNTER_KEY);
        Assertions.assertThat(afterSetDec).isEqualTo(500);

        long current = counterService.getValue(COUNTER_KEY);
        Assertions.assertThat(current).isEqualTo(500);

        boolean isDeleted = counterService.deleteCounter(COUNTER_KEY);
        Assertions.assertThat(isDeleted).isTrue();

        long afterDelete = counterService.getValue(COUNTER_KEY);
        Assertions.assertThat(afterDelete).isEqualTo(0);
    }
}
