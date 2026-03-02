package simulations.config.tests;

import java.time.Duration;

/**
 * Конфигурация MaxPerf Test
 * Цель: Поиск абсолютного предела системы
 */
public class MaxPerfTestConfig extends BaseTestConfig {

    public static final MaxPerfTestConfig INSTANCE = new MaxPerfTestConfig();

    private MaxPerfTestConfig() {
        super(
                1,             // rampUpUsersPerSec
                125,                            // steadyUsersPerSec (максимальная нагрузка)
                125,                            // rampDownUsersPerSec
                Duration.ofMinutes(15),         // rampUpDuration (очень медленный разгон)
                Duration.ofMinutes(5),          // steadyDuration
                Duration.ofMinutes(5)           // rampDownDuration
        );
    }
}