package com.example.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/fee")
public class DummyFeeController {
    @GetMapping
    public BigDecimal calculateFee(@RequestParam("vehicleFamily") String vehicleFamily,
                                   @RequestParam("enterIC") String enterIC,
                                   @RequestParam("exitIC") String exitIC) {
        // ダミーの料金計算ロジック
        // 実際の実装では、料金計算のロジックをここに記述します。
        return BigDecimal.valueOf(1000);
    }
}
