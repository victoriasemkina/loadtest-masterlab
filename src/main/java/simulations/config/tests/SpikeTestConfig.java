package simulations.config.tests;

import java.time.Duration;

/**
 * Конфигурация Spike Test
 * Цель: Проверка реакции на резкий скачок трафика
 */
public class SpikeTestConfig extends BaseTestConfig {

    public static final SpikeTestConfig INSTANCE = new SpikeTestConfig();

    private SpikeTestConfig() {
        super(
                10,            // rampUpUsersPerSec (baseline)
                100,                            // steadyUsersPerSec (spike)
                10,                             // rampDownUsersPerSec (recovery)
                Duration.ofSeconds(30),         // rampUpDuration
                Duration.ofMinutes(1),          // steadyDuration (spike duration)
                Duration.ofSeconds(30)          // rampDownDuration
        );
    }
}