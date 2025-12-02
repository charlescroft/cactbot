package com.aia.wallet.service;

import com.aia.wallet.entity.OperatorLog;

public interface OperatorLogService {
    void log(OperatorLog log);
    void saveBatch(java.util.List<OperatorLog> logs);
    void updateById(OperatorLog log);
}
