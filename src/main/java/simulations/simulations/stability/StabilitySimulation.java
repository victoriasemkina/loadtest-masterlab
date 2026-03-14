package simulations.simulations.stability;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import simulations.core.BaseSimulation;
import simulations.config.tests.StabilityTestConfig;
import simulations.scenarios.transfer.TransferScenarios;
import simulations.assertions.GlobalAssertions;

import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * STABILITY TEST (ENDURANCE/SOAK) — Тестирование стабильности
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ЦЕЛЬ:
 *   Поиск утечек памяти, деградации производительности со временем,
 *   проблем с подключением к БД при длительной нагрузке.
 *
 * ПАРАМЕТРЫ:
 *   • Нагрузка: 10 users/sec (умеренная, постоянная)
 *   • Длительность: 60 минут (5 мин разгон + 55 мин плато)
 *   • Распределение: 80% просмотр курсов, 20% полные переводы
 *
 * КРИТЕРИИ PASS:
 *   • Response Time 95% ≤ 1000ms (стабильно на всём тесте)
 *   • Ошибки ≤ 1%
 *   • Нет роста RT со временем (5 мин ≈ 60 мин)
 *
 * ЧТО ИЩЕМ:
 *   • Утечки памяти (JVM heap growth)
 *   • Деградация БД (connection pool exhaustion)
 *   • Рост Response Time к концу теста
 *   • Увеличение количества ошибок со временем
 *
 * КОГДА ЗАПУСКАТЬ:
 *   • Перед длительными релизными циклами
 *   • После изменений в кэшировании/БД
 *   • Раз в месяц для профилактики
 *
 * ⚠️ ЗАПУСКАТЬ ФОНОМ (60 минут)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class StabilitySimulation extends BaseSimulation {

    private final StabilityTestConfig config = StabilityTestConfig.INSTANCE;

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
                                .to(2)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(2)
                                .during(config.getSteadyDuration())
                )
        ).protocols(httpProtocol())
                .assertions(
                        GlobalAssertions.standardAssertions()
                );
    }
}