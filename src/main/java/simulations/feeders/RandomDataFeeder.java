package simulations.feeders;

import io.gatling.javaapi.core.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class RandomDataFeeder {

    private static final Random random = new Random();

    /**
     * Генератор случайных данных для перевода
     */
    public static Function<Session, Session> randomTransferFeeder() {
        return session -> session
                .set("fromAccount", "ACC" + (random.nextInt(5) + 1))
                .set("toAccount", "ACC" + (random.nextInt(5) + 6))
                .set("amount", random.nextInt(1000) + 100);
    }
}