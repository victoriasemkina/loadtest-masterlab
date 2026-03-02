package simulations.simulations.maxperf;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import simulations.core.BaseSimulation;
import simulations.config.tests.MaxPerfTestConfig;
import simulations.scenarios.transfer.TransferScenarios;

import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * MAX PERFORMANCE TEST — Тест на максимальную производительность
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ЦЕЛЬ:
 *   Поиск абсолютного предела системы. Определение максимального RPS,
 *   который система может выдать перед полным отказом.
 *
 * ПАРАМЕТРЫ:
 *   • Нагрузка: 125 users/sec (в 25 раз выше обычной)
 *   • Длительность: 25 минут (15 мин разгон + 5 мин плато + 5 мин спад)
 *   • Распределение: 80% просмотр курсов, 20% полные переводы
 *
 * КРИТЕРИИ PASS:
 *   • Response Time Max ≤ 30000ms (очень мягкий критерий)
 *   • Фиксация метрик даже при падении теста
 *
 * ЧТО ИЩЕМ:
 *   • Абсолютный максимум RPS
 *   • Точку 100% деградации
 *   • Поведение системы при экстремальной нагрузке
 *   • Максимальное количество одновременных пользователей
 *
 * КОГДА ЗАПУСКАТЬ:
 *   • Для capacity planning на год вперёд
 *   • Перед архитектурными изменениями
 *   • Раз в квартал для бенчмарка
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class MaxPerformanceSimulation extends BaseSimulation {

    private final MaxPerfTestConfig config = MaxPerfTestConfig.INSTANCE;

    private final ScenarioBuilder scnViewRates = scenario("View Rates (80%)")
            .exec(TransferScenarios.viewRatesOnly());

    private final ScenarioBuilder scnFullTransfer = scenario("Full Transfer (20%)")
            .exec(TransferScenarios.fullTransferFlow());

    {
        setUp(
                scnViewRates.injectOpen(
                        rampUsersPerSec(1)
                                .to(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getSteadyDuration())
                ),
                scnFullTransfer.injectOpen(
                        rampUsersPerSec(1)
                                .to(25)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(25)
                                .during(config.getSteadyDuration())
                )
        ).protocols(httpProtocol())
                .assertions(
                        global().responseTime().max().lte(30000)
                );
    }
}