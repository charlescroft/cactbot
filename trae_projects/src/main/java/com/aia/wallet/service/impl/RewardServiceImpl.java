package com.aia.wallet.service.impl;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.dto.BalanceResponse;
import com.aia.wallet.dto.TransactionResponse;
import com.aia.wallet.entity.RewardTransaction;
import com.aia.wallet.enums.TransactionStatus;
import com.aia.wallet.repository.RewardTransactionRepository;
import com.aia.wallet.repository.UserBalanceViewRepository;
import com.aia.wallet.service.RewardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    private final RewardTransactionRepository rewardTransactionRepository;
    private final UserBalanceViewRepository userBalanceViewRepository;

    public RewardServiceImpl(RewardTransactionRepository rewardTransactionRepository,
                             UserBalanceViewRepository userBalanceViewRepository) {
        this.rewardTransactionRepository = rewardTransactionRepository;
        this.userBalanceViewRepository = userBalanceViewRepository;
    }

    @Override
    public BalanceResponse getBalance(String clientId) {
        return userBalanceViewRepository.findById(clientId)
                .map(view -> BalanceResponse.builder()
                        .clientId(view.getClientId())
                        .balance(view.getBalance())
                        .currency(view.getCurrency())
                        .lastUpdatedAt(view.getLastUpdatedAt())
                        .build())
                .orElse(BalanceResponse.builder()
                        .clientId(clientId)
                        .balance(BigDecimal.ZERO)
                        .currency(AppConstants.CURRENCY_MMK)
                        .lastUpdatedAt(null)
                        .build());
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(String clientId) {
        return rewardTransactionRepository.findByClientId(clientId).stream()
                .filter(tx -> tx.getStatus() == TransactionStatus.SUCCESS)
                .sorted(Comparator.comparing(RewardTransaction::getTransactionDate).reversed())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse toResponse(RewardTransaction tx) {
        return TransactionResponse.builder()
                .transactionId(String.valueOf(tx.getTransactionId()))
                .clientId(tx.getClientId())
                .transactionType(tx.getTransactionType().name())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .transactionDate(tx.getTransactionDate())
                .requestedBy(tx.getRequestedBy())
                .campaignDescription(tx.getCampaignDescription())
                .campaignCode(tx.getCampaignCode())
                .effectiveDate(tx.getEffectiveDate())
                .expirationDate(tx.getExpirationDate())
                .build();
    }
}
