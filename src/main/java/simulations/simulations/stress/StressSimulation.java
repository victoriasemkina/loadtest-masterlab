package simulations.simulations.stress;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import simulations.core.BaseSimulation;
import simulations.config.tests.StressTestConfig;
import simulations.scenarios.transfer.TransferScenarios;

import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * STRESS TEST — Стресс-тестирование
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ЦЕЛЬ:
 *   Поиск точки отказа системы. Определение максимальной нагрузки,
 *   которую система может выдержать перед деградацией.
 *
 * ПАРАМЕТРЫ:
 *   • Нагрузка: 50 users/sec (в 10 раз выше обычной)
 *   • Длительность: 15 минут (10 мин разгон + 5 мин плато)
 *   • Распределение: 80% просмотр курсов, 20% полные переводы
 *
 * КРИТЕРИИ PASS:
 *   • Response Time 95% ≤ 5000ms (мягче чем Load test)
 *   • Ошибки ≤ 50% (допускаем деградацию)
 *
 * ЧТО ИЩЕМ:
 *   • При какой нагрузке начинаются ошибки
 *   • Как растёт Response Time под нагрузкой
 *   • Максимальный RPS перед отказом
 *
 * КОГДА ЗАПУСКАТЬ:
 *   • Перед планированием масштабирования
 *   • Для определения лимитов системы
 *   • Раз в квартал для capacity planning
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class StressSimulation extends BaseSimulation {

    private final StressTestConfig config = StressTestConfig.INSTANCE;

    {
        setUp(
                TransferScenarios.scnViewRates.injectOpen(
                        rampUsersPerSec(1)
                                .to(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getSteadyDuration())
                ),
                TransferScenarios.scnFullTransfer.injectOpen(
                        rampUsersPerSec(1)
                                .to(12)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(12)
                                .during(config.getSteadyDuration())
                )
        ).protocols(httpProtocol())
                .assertions(
                        global().responseTime().percentile(95).lte(5000),
                        global().failedRequests().percent().lte(50.0)
                );
    }
}