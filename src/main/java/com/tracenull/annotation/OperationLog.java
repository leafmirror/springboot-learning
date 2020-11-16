package com.tracenull.annotation;

import com.tracenull.eu.OperationType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    OperationType opType();

    String opBusinessName();


    String opBusinessId();
}
