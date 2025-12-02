package com.aia.wallet.controller;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.dto.BalanceResponse;
import com.aia.wallet.dto.BaseResponse;
import com.aia.wallet.dto.TransactionResponse;
import com.aia.wallet.service.RewardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/balance")
    public BaseResponse<BalanceResponse> getBalance(@RequestParam(AppConstants.Api.PARAM_CLIENT_ID) String clientId) {
        if (clientId == null || clientId.trim().isEmpty()) {
            return BaseResponse.error(400, AppConstants.Messages.ERROR_CLIENT_ID_REQUIRED);
        }
        try {
            BalanceResponse balance = rewardService.getBalance(clientId);
            if (balance.getLastUpdatedAt() == null && balance.getBalance().compareTo(java.math.BigDecimal.ZERO) == 0) {
                // Check if user exists or just no transactions?
                // Spec says 404 if no record exists.
                // However, balance 0 is valid.
                // If no transactions, we might return 0 balance, or 404?
                // Spec 3.1 Error Responses: 404 Not Found: Returned if no record exists for the provided userId.
                // If `getBalance` returns a default object with 0 balance, how do we know if user exists?
                // In a real system, we check a User table. Here we only have transaction table.
                // If no transactions, effectively user "doesn't exist" in this context or has 0 balance.
                // I'll assume if the list was empty (lastUpdatedAt is null), it's a 404 case.
                if (balance.getLastUpdatedAt() == null) {
                     return BaseResponse.error(404, AppConstants.Messages.ERROR_USER_NOT_FOUND);
                }
            }
            return BaseResponse.success(balance);
        } catch (Exception e) {
            return BaseResponse.error(500, e.getMessage());
        }
    }

    @GetMapping("/transactions")
    public BaseResponse<List<TransactionResponse>> getTransactionHistory(@RequestParam(AppConstants.Api.PARAM_CLIENT_ID) String clientId) {
        if (clientId == null || clientId.trim().isEmpty()) {
            return BaseResponse.error(400, AppConstants.Messages.ERROR_CLIENT_ID_REQUIRED);
        }
        try {
            List<TransactionResponse> transactions = rewardService.getTransactionHistory(clientId);
            if (transactions.isEmpty()) {
                 // Spec says 404 if no record exists.
                 return BaseResponse.error(404, AppConstants.Messages.ERROR_USER_NOT_FOUND);
            }
            return BaseResponse.success(transactions);
        } catch (Exception e) {
             return BaseResponse.error(500, e.getMessage());
        }
    }
}
