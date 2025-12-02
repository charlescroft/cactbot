package com.aia.wallet.service.impl;

import com.aia.wallet.entity.OperatorLog;
import com.aia.wallet.repository.OperatorLogRepository;
import com.aia.wallet.service.OperatorLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperatorLogServiceImpl implements OperatorLogService {

    private final OperatorLogRepository operatorLogRepository;

    public OperatorLogServiceImpl(OperatorLogRepository operatorLogRepository) {
        this.operatorLogRepository = operatorLogRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(OperatorLog log) {
        operatorLogRepository.save(log);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBatch(java.util.List<OperatorLog> logs) {
        operatorLogRepository.saveAll(logs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateById(OperatorLog log) {
        operatorLogRepository.save(log);
    }
}
