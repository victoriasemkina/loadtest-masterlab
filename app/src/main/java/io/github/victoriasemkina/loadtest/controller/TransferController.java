package io.github.victoriasemkina.loadtest.controller;

import io.github.victoriasemkina.loadtest.model.TransferRequest;
import io.github.victoriasemkina.loadtest.model.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransferController {

    private final Random random = new Random();

    @GetMapping("/rates")
    public ResponseEntity<Map<String, BigDecimal>> getRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("1.00"));
        rates.put("EUR", new BigDecimal("0.85"));
        rates.put("GBP", new BigDecimal("0.73"));
        rates.put("JPY", new BigDecimal("110.00"));
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Map<String, String>>> getAccounts() {
        List<Map<String, String>> accounts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, String> account = new HashMap<>();
            account.put("id", "ACC" + String.format("%03d", i));
            account.put("balance", "10000.00");
            account.put("currency", "USD");
            accounts.add(account);
        }
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/transfer/validate")
    public ResponseEntity<Map<String, Object>> validateTransfer(@RequestBody TransferRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("fee", new BigDecimal("1.50"));
        response.put("estimatedTime", "instant");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer/execute")
    public ResponseEntity<TransferResponse> executeTransfer(@RequestBody TransferRequest request) {
        TransferResponse response = new TransferResponse();
        response.setTransferId("TRF" + System.currentTimeMillis());
        response.setStatus("COMPLETED");
        response.setAmount(request.getAmount());
        response.setFromAccount(request.getFromAccount());
        response.setToAccount(request.getToAccount());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/transfer/status/{transferId}")
    public ResponseEntity<TransferResponse> getTransferStatus(@PathVariable String transferId) {
        TransferResponse response = new TransferResponse();
        response.setTransferId(transferId);
        response.setStatus("COMPLETED");
        response.setAmount(new BigDecimal("100.00"));
        response.setFromAccount("ACC001");
        response.setToAccount("ACC002");
        return ResponseEntity.ok(response);
    }
}