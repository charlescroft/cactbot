package com.aia.wallet.repository;

import com.aia.wallet.entity.UserBalanceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceViewRepository extends JpaRepository<UserBalanceView, String> {
}
