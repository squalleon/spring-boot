package com.apress.spring.heatlh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.apress.spring.repository.JournalRepository;

@Component
public class QuotaHealthIndicator implements HealthIndicator{

    private static final Long QUOTA_MAX_SIZE = 3L;

    @Autowired
    JournalRepository repo;

    @Override
    public Health health() {
        long size = repo.count();
        if(size <= QUOTA_MAX_SIZE)
            return Health.up().withDetail("quota.entries", size).build();
        else
            return Health.down().withDetail("quota.entries", size).withException(new QuotaException("할당량 초과. 최대 할당량: " + QUOTA_MAX_SIZE + ". 할당량 정책은 관리자에게 문의하세요.")).build();
    }


}
