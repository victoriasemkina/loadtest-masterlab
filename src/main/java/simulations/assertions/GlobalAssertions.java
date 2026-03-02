package simulations.assertions;

import io.gatling.javaapi.core.Assertion;
import static io.gatling.javaapi.core.CoreDsl.global;

public class GlobalAssertions {

    public static Assertion[] standardAssertions() {
        return new Assertion[] {
                global().responseTime().percentile3().lte(1000),
                global().responseTime().percentile(99.0).lte(2000),
                global().failedRequests().percent().lte(1.0),
                global().successfulRequests().count().gte(100L)
        };
    }
}