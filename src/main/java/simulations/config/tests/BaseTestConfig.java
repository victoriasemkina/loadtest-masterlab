package simulations.config.tests;

import java.time.Duration;

/**
 * Базовый класс конфигурации теста
 * Все параметры тестов наследуются от этого класса
 */
public abstract class BaseTestConfig {

    protected final int rampUpUsersPerSec;
    protected final int steadyUsersPerSec;
    protected final int rampDownUsersPerSec;
    protected final Duration rampUpDuration;
    protected final Duration steadyDuration;
    protected final Duration rampDownDuration;

    public BaseTestConfig(int rampUpUsersPerSec,
                          int steadyUsersPerSec,
                          int rampDownUsersPerSec,
                          Duration rampUpDuration,
                          Duration steadyDuration,
                          Duration rampDownDuration) {
        this.rampUpUsersPerSec = rampUpUsersPerSec;
        this.steadyUsersPerSec = steadyUsersPerSec;
        this.rampDownUsersPerSec = rampDownUsersPerSec;
        this.rampUpDuration = rampUpDuration;
        this.steadyDuration = steadyDuration;
        this.rampDownDuration = rampDownDuration;
    }

    public int getRampUpUsersPerSec() { return rampUpUsersPerSec; }
    public int getSteadyUsersPerSec() { return steadyUsersPerSec; }
    public int getRampDownUsersPerSec() { return rampDownUsersPerSec; }
    public Duration getRampUpDuration() { return rampUpDuration; }
    public Duration getSteadyDuration() { return steadyDuration; }
    public Duration getRampDownDuration() { return rampDownDuration; }
}