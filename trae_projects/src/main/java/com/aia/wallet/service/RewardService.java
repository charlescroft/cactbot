package com.aia.wallet.service;

import com.aia.wallet.dto.BalanceResponse;
import com.aia.wallet.dto.TransactionResponse;

import java.util.List;

public interface RewardService {
    
    BalanceResponse getBalance(String clientId);
    
    List<TransactionResponse> getTransactionHistory(String clientId);
}
