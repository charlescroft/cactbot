package com.aia.wallet.repository;

import com.aia.wallet.entity.OperatorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperatorLogRepository extends JpaRepository<OperatorLog, Long> {
    List<OperatorLog> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
