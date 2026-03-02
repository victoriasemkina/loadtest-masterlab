package simulations.config.tests;

import java.time.Duration;

/**
 * Конфигурация Stress Test
 * Цель: Поиск точки отказа системы
 */
public class StressTestConfig extends BaseTestConfig {

    public static final StressTestConfig INSTANCE = new StressTestConfig();

    private StressTestConfig() {
        super(
                1,             // rampUpUsersPerSec
                50,                             // steadyUsersPerSec (высокая нагрузка)
                50,                             // rampDownUsersPerSec
                Duration.ofMinutes(10),         // rampUpDuration (медленный разгон)
                Duration.ofMinutes(5),          // steadyDuration
                Duration.ofMinutes(5)           // rampDownDuration
        );
    }
}