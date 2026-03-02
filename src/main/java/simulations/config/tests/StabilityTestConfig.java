package simulations.config.tests;

import java.time.Duration;

/**
 * Конфигурация Stability Test (Endurance)
 * Цель: Поиск утечек памяти при длительной нагрузке
 */
public class StabilityTestConfig extends BaseTestConfig {

    public static final StabilityTestConfig INSTANCE = new StabilityTestConfig();

    private StabilityTestConfig() {
        super(
                1,             // rampUpUsersPerSec
                10,                             // steadyUsersPerSec (умеренная нагрузка)
                10,                             // rampDownUsersPerSec
                Duration.ofMinutes(5),          // rampUpDuration
                Duration.ofMinutes(55),         // steadyDuration (длительный тест)
                Duration.ofMinutes(5)           // rampDownDuration
        );
    }
}