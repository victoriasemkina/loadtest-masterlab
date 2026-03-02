package simulations.scenarios.transfer;

import io.gatling.javaapi.core.ChainBuilder;
import simulations.feeders.RandomDataFeeder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class TransferScenarios {

    /**
     * Ручка 1: Получить курсы валют (80% пользователей)
     */
    public static ChainBuilder getRates() {
        return exec(
                http("GET /api/rates")
                        .get("/api/rates")
                        .check(status().is(200))
                        .check(jsonPath("$.USD").exists())
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Ручка 2: Получить счета (20% пользователей)
     */
    public static ChainBuilder getAccounts() {
        return exec(
                http("GET /api/accounts")
                        .get("/api/accounts")
                        .check(status().is(200))
                        .check(jsonPath("$[*].id").findAll().saveAs("accountIds"))
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Ручка 3: Валидация перевода
     */
    public static ChainBuilder validateTransfer() {
        return exec(RandomDataFeeder.randomTransferFeeder())
                .exec(
                        http("POST /api/transfer/validate")
                                .post("/api/transfer/validate")
                                .body(StringBody("{\"fromAccount\": \"#{fromAccount}\", \"toAccount\": \"#{toAccount}\", \"amount\": #{amount}}"))
                                .asJson()
                                .check(status().is(200))
                                .check(jsonPath("$.valid").is(String.valueOf(true)))
                ).pause(Duration.ofMillis(100));
    }

    /**
     * Ручка 4: Исполнение перевода
     */
    public static ChainBuilder executeTransfer() {
        return exec(
                http("POST /api/transfer/execute")
                        .post("/api/transfer/execute")
                        .body(StringBody("{\"fromAccount\": \"#{fromAccount}\", \"toAccount\": \"#{toAccount}\", \"amount\": #{amount}}"))
                        .asJson()
                        .check(status().is(201))
                        .check(jsonPath("$.transferId").saveAs("transferId"))
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Ручка 5: Статус перевода
     */
    public static ChainBuilder checkTransferStatus() {
        return exec(
                http("GET /api/transfer/status")
                        .get("/api/transfer/status/#{transferId}")
                        .check(status().is(200))
                        .check(jsonPath("$.status").is("COMPLETED"))
        ).pause(Duration.ofMillis(100));
    }

    /**
     * Сценарий 1: Только просмотр курсов (80% пользователей)
     */
    public static ChainBuilder viewRatesOnly() {
        return getRates();
    }

    /**
     * Сценарий 2: Полный цикл перевода (20% пользователей)
     */
    public static ChainBuilder fullTransferFlow() {
        return getRates()
                .exec(getAccounts())
                .exec(validateTransfer())
                .exec(executeTransfer())
                .exec(checkTransferStatus());
    }
}