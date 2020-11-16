package com.tracenull.dao;

import com.tracenull.po.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationDao extends JpaRepository<Operation,String> {
}
