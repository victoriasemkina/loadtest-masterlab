package io.github.victoriasemkina.loadtest.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferResponse {
    private String transferId;
    private String status;
    private BigDecimal amount;
    private String fromAccount;
    private String toAccount;
}