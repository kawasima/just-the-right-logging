package com.example.web.adapter;

import com.example.web.domain.Billing;

import java.util.List;

public interface BillingRepository {
    List<Billing> findByMemberId(String memberId);
    void save(Billing billing);
}
