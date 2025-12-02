package com.aia.wallet.repository;

import com.aia.wallet.entity.RewardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RewardTransactionRepository extends JpaRepository<RewardTransaction, Long> {
    List<RewardTransaction> findByClientId(String clientId);
    List<RewardTransaction> findByClientIdAndTransactionDateBetween(String clientId, LocalDate startDate, LocalDate endDate);
}
