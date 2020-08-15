package com.tracenull.aspect;

import com.tracenull.DataSourceContextHolder;
import com.tracenull.DataSourceSelector;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Order(value = 1)
@Component
public class DataSourceContextAop {
    @Around("@annotation(com.tracenull.DataSourceSelector)")
    public Object setDynamicDataSource(ProceedingJoinPoint point) throws Throwable {
        boolean clear = true;
        try {
            Method method = this.getMethod(point);
            DataSourceSelector dataSourceImport = method.getAnnotation(DataSourceSelector.class);
            clear = dataSourceImport.clear();
            DataSourceContextHolder.set(dataSourceImport.value().getDataSourceName());
            log.info("========数据源切换至：{}", dataSourceImport.value().getDataSourceName());
            return point.proceed();
        } finally {
            if (clear) {
                DataSourceContextHolder.clear();
            }
        }
    }

    private Method getMethod(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getMethod();
    }
}
