package simulations.config.tests;

import java.time.Duration;

/**
 * Конфигурация Load Test
 * Цель: Проверка SLA под ожидаемой нагрузкой
 */
public class LoadTestConfig extends BaseTestConfig {

    public static final LoadTestConfig INSTANCE = new LoadTestConfig();

    private LoadTestConfig() {
        super(
                1,             // rampUpUsersPerSec
                5,                              // steadyUsersPerSec
                5,                              // rampDownUsersPerSec
                Duration.ofSeconds(10),         // rampUpDuration
                Duration.ofSeconds(30),         // steadyDuration
                Duration.ofSeconds(10)          // rampDownDuration
        );
    }
}