package simulations.scenarios.transfer;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import simulations.actions.TransferActions;
import simulations.feeders.RandomDataFeeder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class TransferScenarios {

    /**
     * Сценарий 1: Только просмотр курсов (80% пользователей)
     */
    public static ScenarioBuilder scnViewRates = scenario("View Rates (80%)")
            .exec(TransferActions.getRates());

    /**
     * Сценарий 2: Полный цикл перевода (20% пользователей)
     */
    public static ScenarioBuilder scnFullTransfer = scenario("Full Transfer (20%)")
            .exec(TransferActions.getRates())
            .exec(TransferActions.getAccounts())
            .exec(TransferActions.validateTransfer())
            .exec(TransferActions.executeTransfer())
            .exec(TransferActions.checkTransferStatus());
    }
}