package simulations.simulations.spike;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import simulations.core.BaseSimulation;
import simulations.config.tests.SpikeTestConfig;
import simulations.scenarios.transfer.TransferScenarios;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * SPIKE TEST — Тестирование пиковой нагрузки
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ЦЕЛЬ:
 *   Проверка реакции системы на резкий скачок трафика (виральный эффект,
 *   маркетинговая акция, новостной всплеск).
 *
 * ПАРАМЕТРЫ:
 *   • Базовая нагрузка: 10 users/sec
 *   • Пиковая нагрузка: 100 users/sec (в 10 раз выше)
 *   • Длительность пика: 1 минута
 *   • Общая длительность: 10 минут
 *   • Распределение: 80% просмотр курсов, 20% полные переводы
 *
 * КРИТЕРИИ PASS:
 *   • Response Time 95% ≤ 2000ms
 *   • Ошибки ≤ 5%
 *   • Восстановление после пика < 2 минут
 *
 * ЧТО ИЩЕМ:
 *   • Как быстро система адаптируется к пику
 *   • Есть ли «эхо-эффект» (ошибки после спада)
 *   • Время восстановления до нормальной работы
 *
 * КОГДА ЗАПУСКАТЬ:
 *   • Перед маркетинговыми акциями
 *   • Перед ожидаемым всплеском трафика
 *   • Для проверки автоскейлинга
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class SpikeSimulation extends BaseSimulation {

    private final SpikeTestConfig config = SpikeTestConfig.INSTANCE;

    private final ScenarioBuilder scnViewRates = scenario("View Rates (80%)")
            .exec(TransferScenarios.viewRatesOnly());

    private final ScenarioBuilder scnFullTransfer = scenario("Full Transfer (20%)")
            .exec(TransferScenarios.fullTransferFlow());

    {
        setUp(
                scnViewRates.injectOpen(
                        constantUsersPerSec(8).during(Duration.ofMinutes(2)),
                        rampUsersPerSec(8).to(80).during(config.getRampUpDuration()),
                        constantUsersPerSec(80).during(config.getSteadyDuration()),
                        rampUsersPerSec(80).to(8).during(config.getRampDownDuration()),
                        constantUsersPerSec(8).during(Duration.ofMinutes(2))
                ),
                scnFullTransfer.injectOpen(
                        constantUsersPerSec(2).during(Duration.ofMinutes(2)),
                        rampUsersPerSec(2).to(20).during(config.getRampUpDuration()),
                        constantUsersPerSec(20).during(config.getSteadyDuration()),
                        rampUsersPerSec(20).to(2).during(config.getRampDownDuration()),
                        constantUsersPerSec(2).during(Duration.ofMinutes(2))
                )
        ).protocols(httpProtocol())
                .assertions(
                        global().responseTime().percentile(95).lte(2000),
                        global().failedRequests().percent().lte(5.0)
                );
    }
}