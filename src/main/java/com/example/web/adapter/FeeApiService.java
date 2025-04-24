package com.example.web.adapter;

import com.example.web.domain.VehicleFamily;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class FeeApiService {
    private final RestTemplate restTemplate;

    public FeeApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public BigDecimal calculateFee(VehicleFamily vehicleFamily, String enterIC, String exitIC) {
        return restTemplate.getForObject(
                "http://localhost:8080/api/fee?vehicleFamily={vehicleFamily}&enterIC={enterIC}&exitIC={exitIC}",
                BigDecimal.class,
                vehicleFamily,
                enterIC,
                exitIC
        );
    }
}
