package simulations.actions;

import io.gatling.javaapi.core.ChainBuilder;
import simulations.config.GatlingConfig;
import simulations.feeders.RandomDataFeeder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Атомарные действия для Transfer API
 * Каждый метод = один HTTP-запрос
 */
public class TransferActions {

    /**
     * Действие 1: Получить курсы валют
     */
    public static ChainBuilder getRates() {
        return exec(
                http("GET /api/rates")
                        .get(GatlingConfig.RATES.getValue())
                        .check(status().is(200))
                        .check(jsonPath("$.USD").exists())
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Действие 2: Получить счета пользователя
     */
    public static ChainBuilder getAccounts() {
        return exec(
                http("GET /api/accounts")
                        .get(GatlingConfig.ACCOUNTS.getValue())
                        .check(status().is(200))
                        .check(jsonPath("$[*].id").findAll().saveAs("accountIds"))
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Действие 3: Валидация перевода
     */
    public static ChainBuilder validateTransfer() {
        return exec(RandomDataFeeder.randomTransferFeeder())
                .exec(
                        http("POST /api/transfer/validate")
                                .post(GatlingConfig.TRANSFER_VALIDATE.getValue())
                                .body(ElFileBody(GatlingConfig.BODY_TRANSFER_VALIDATE.getValue())).asJson()
                                .check(status().is(200))
                                .check(jsonPath("$.valid").is(String.valueOf(true)))
                ).pause(Duration.ofMillis(100));
    }

    /**
     * Действие 4: Исполнение перевода
     */
    public static ChainBuilder executeTransfer() {
        return exec(
                http("POST /api/transfer/execute")
                        .post(GatlingConfig.TRANSFER_EXECUTE.getValue())
                        .body(ElFileBody(GatlingConfig.BODY_TRANSFER_EXECUTE.getValue())).asJson()
                        .check(status().is(201))
                        .check(jsonPath("$.transferId").saveAs("transferId"))
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Действие 5: Проверка статуса перевода
     */
    public static ChainBuilder checkTransferStatus() {
        return exec(
                http("GET /api/transfer/status")
                        .get(GatlingConfig.TRANSFER_STATUS.getValue())
                        .check(status().is(200))
                        .check(jsonPath("$.status").is("COMPLETED"))
        ).pause(Duration.ofMillis(100));
    }
}