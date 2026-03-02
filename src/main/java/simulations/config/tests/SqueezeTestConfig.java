package simulations.config.tests;

import java.time.Duration;

/**
 * Конфигурация Squeeze Test
 * Цель: Поиск минимальных ресурсов для сохранения SLA
 * ⚠️ Требует Docker для изменения CPU/RAM во время теста
 */
public class SqueezeTestConfig extends BaseTestConfig {

    public static final SqueezeTestConfig INSTANCE = new SqueezeTestConfig();

    private SqueezeTestConfig() {
        super(
                1,             // rampUpUsersPerSec
                10,                             // steadyUsersPerSec
                10,                             // rampDownUsersPerSec
                Duration.ofMinutes(5),          // rampUpDuration
                Duration.ofMinutes(25),         // steadyDuration
                Duration.ofMinutes(5)           // rampDownDuration
        );
    }
}