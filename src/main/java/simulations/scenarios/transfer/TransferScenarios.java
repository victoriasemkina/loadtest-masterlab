package simulations.scenarios.transfer;

import io.gatling.javaapi.core.ChainBuilder;
import simulations.actions.TransferActions;
import simulations.feeders.RandomDataFeeder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class TransferScenarios {

    /**
     * Сценарий 1: Только просмотр курсов (80% пользователей)
     */
    public static ChainBuilder viewRatesOnly() {
        return TransferActions.getRates();
    }

    /**
     * Сценарий 2: Полный цикл перевода (20% пользователей)
     */
    public static ChainBuilder fullTransferFlow() {
        return exec(TransferActions.getRates())
                .exec(TransferActions.getAccounts())
                .exec(TransferActions.validateTransfer())
                .exec(TransferActions.executeTransfer())
                .exec(TransferActions.checkTransferStatus());
    }
}