package com.tracenull.service;

import com.tracenull.dao.OperationDao;
import com.tracenull.po.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationService {
    @Autowired
    OperationDao operationDao;

    public void save(Operation operation) {
        operationDao.save(operation);
    }
}
