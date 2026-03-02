package simulations.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GatlingConfig {

    // ==================== Base URL ====================
    BASE_URL("api.base.url"),

    // Transfer API
    RATES("api.rates"),
    ACCOUNTS("api.accounts"),
    TRANSFER_VALIDATE("api.transfer.validate"),
    TRANSFER_EXECUTE("api.transfer.execute"),
    TRANSFER_STATUS("api.transfer.status"),

    // Request Bodies
    BODY_TRANSFER_VALIDATE("api.body.transfer.validate"),
    BODY_TRANSFER_EXECUTE("api.body.transfer.execute"),

    // ==================== Timeouts ====================
    TIMEOUT_CONNECT("api.timeout.connect"),
    TIMEOUT_READ("api.timeout.read"),

    // ==================== Test Config ====================
    TEST_RAMPUP_DURATION("test.rampup.duration.seconds"),
    TEST_STEADY_DURATION("test.steady.duration.seconds"),
    TEST_RAMPDOWN_DURATION("test.rampdown.duration.seconds"),
    TEST_USERS_PER_SECOND("test.users.per.second");

    private final String propertyKey;

    public String getValue() {
        return ApiConfig.getPropertyByKey(this.propertyKey);
    }

    public int getIntValue() {
        return ApiConfig.getIntPropertyByKey(this.propertyKey);
    }

    public int getIntValue(int defaultValue) {
        return ApiConfig.getIntPropertyByKey(this.propertyKey, defaultValue);
    }
}