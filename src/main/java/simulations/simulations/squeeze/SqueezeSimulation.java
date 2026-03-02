package simulations.simulations.squeeze;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import simulations.core.BaseSimulation;
import simulations.config.tests.SqueezeTestConfig;
import simulations.scenarios.transfer.TransferScenarios;
import simulations.assertions.GlobalAssertions;

import static io.gatling.javaapi.core.CoreDsl.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * SQUEEZE TEST — Тестирование оптимизации ресурсов
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ЦЕЛЬ:
 *   Поиск минимальных ресурсов (CPU, RAM), необходимых для сохранения SLA.
 *   Оптимизация инфраструктуры и затрат.
 *
 * ПАРАМЕТРЫ:
 *   • Нагрузка: 10 users/sec (умеренная, постоянная)
 *   • Длительность: 35 минут (5 мин разгон + 25 мин плато + 5 мин спад)
 *   • Распределение: 80% просмотр курсов, 20% полные переводы
 *
 * КРИТЕРИИ PASS:
 *   • Response Time 95% ≤ 1000ms
 *   • Ошибки ≤ 1%
 *   • SLA сохраняется при уменьшении ресурсов
 *
 * ЧТО ИЩЕМ:
 *   • Минимальное количество CPU для SLA
 *   • Минимальный объем RAM для SLA
 *   • Точку где начинается деградация
 *   • Оптимальное соотношение цена/производительность
 *
 * КОГДА ЗАПУСКАТЬ:
 *   • Перед оптимизацией инфраструктуры
 *   • Для снижения затрат на облако
 *   • При миграции на меньшие инстансы
 *
 * ⚠️ ТРЕБУЕТ DOCKER: Нужно менять CPU/RAM во время теста
 *    Пример: docker update --memory=2g --cpus=1 <container_id>
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */

public class SqueezeSimulation extends BaseSimulation {

    // Конфигурация Squeeze теста (оптимизация ресурсов)
    private final SqueezeTestConfig config = SqueezeTestConfig.INSTANCE;

    // Сценарий 1: 80% пользователей — только просмотр курсов
    private final ScenarioBuilder scnViewRates = scenario("View Rates (80%)")
            .exec(TransferScenarios.viewRatesOnly());

    // Сценарий 2: 20% пользователей — полный цикл перевода
    private final ScenarioBuilder scnFullTransfer = scenario("Full Transfer (20%)")
            .exec(TransferScenarios.fullTransferFlow());

    {
        setUp(
                // 80% трафика: просмотр курсов
                scnViewRates.injectOpen(
                        rampUsersPerSec(1)
                                .to(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(config.getSteadyUsersPerSec() * 8 / 10)
                                .during(config.getSteadyDuration())
                ),
                // 20% трафика: полные переводы
                scnFullTransfer.injectOpen(
                        rampUsersPerSec(1)
                                .to(2)
                                .during(config.getRampUpDuration()),
                        constantUsersPerSec(2)
                                .during(config.getSteadyDuration())
                )
        ).protocols(httpProtocol())
                .assertions(
                        // Стандартные SLA assertions для Squeeze теста
                        GlobalAssertions.standardAssertions()
                );
    }
}