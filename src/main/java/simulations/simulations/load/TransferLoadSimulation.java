package simulations.simulations.load;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import simulations.core.BaseSimulation;
import simulations.config.tests.LoadTestConfig;
import simulations.scenarios.transfer.TransferScenarios;
import simulations.assertions.GlobalAssertions;

import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * LOAD TEST — Нагрузочное тестирование
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ЦЕЛЬ:
 *   Проверка стабильности системы под ожидаемой производственной нагрузкой
 *   и соответствие SLA (Service Level Agreement).
 *
 * ПАРАМЕТРЫ:
 *   • Нагрузка: 5 users/sec (10 users/sec всего с учётом 80/20)
 *   • Длительность: 50 секунд (10s разгон + 30s плато + 10s спад)
 *   • Распределение: 80% просмотр курсов, 20% полные переводы
 *
 * КРИТЕРИИ PASS:
 *   • Response Time 95% ≤ 1000ms
 *   • Response Time 99% ≤ 2000ms
 *   • Ошибки ≤ 1%
 *   • Успешных запросов ≥ 100
 *
 * КОГДА ЗАПУСКАТЬ:
 *   • Перед каждым релизом
 *   • После деплоя на staging
 *   • В CI/CD пайплайне
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class TransferLoadSimulation extends BaseSimulation {

    private final LoadTestConfig config = LoadTestConfig.INSTANCE;

    {
        setUp(
                TransferScenarios.scnViewRates.injectOpen(
                        rampUsersPerSec(1)
                                .to(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getSteadyDuration()),
                        rampUsersPerSec(config.getSteadyUsersPerSec() * 8 / 10)
                                .to(0)
                                .during(config.getRampDownDuration())
                ),
                TransferScenarios.scnFullTransfer.injectOpen(
                        rampUsersPerSec(1)
                                .to(config.getSteadyUsersPerSec() * 2 / 10)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(config.getSteadyUsersPerSec() * 2 / 10)
                                .during(config.getSteadyDuration()),
                        rampUsersPerSec(config.getSteadyUsersPerSec() * 2 / 10)
                                .to(0)
                                .during(config.getRampDownDuration())
                )
        ).protocols(httpProtocol())
                .assertions(
                        GlobalAssertions.standardAssertions()
                );
    }
}