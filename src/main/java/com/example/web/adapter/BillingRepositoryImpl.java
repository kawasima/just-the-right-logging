package com.example.web.adapter;

import com.example.web.domain.Billing;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class BillingRepositoryImpl implements BillingRepository {
    private final Map<UUID, Billing> billingMap = new HashMap<>();

    @Override
    public List<Billing> findByMemberId(String memberId) {
        return billingMap.values().stream().toList();
    }

    @Override
    public void save(Billing billing) {
        billingMap.put(billing.getId(), billing);
    }
}
