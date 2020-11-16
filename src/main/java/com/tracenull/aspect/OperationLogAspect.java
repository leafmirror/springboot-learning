package com.tracenull.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Snowflake;
import com.google.common.base.CaseFormat;
import com.tracenull.annotation.OperationLog;
import com.tracenull.po.Operation;
import com.tracenull.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 我用注解实现接口的操作流水日志
 * https://mp.weixin.qq.com/s/1k84kp1lWs790PfvnUd2GA
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private OperationService operationService;

    private static Snowflake snowflake = new Snowflake(1, 1);

    @Autowired
    HttpServletRequest request;

    @Around("@annotation(com.tracenull.annotation.OperationLog)")
    public Object log(ProceedingJoinPoint pjp) throws Exception {

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        // 获取目标方法上面的注解
        OperationLog opLog = method.getAnnotation(OperationLog.class);

        Object result;

        TimeInterval timer = DateUtil.timer();

        try {
            // 执行目标方法
            result = pjp.proceed();
        } catch (Throwable throwable) {
//            throwable.printStackTrace();
            throw new Exception(throwable);
        }

        long executeTime = timer.intervalRestart();
        if (StringUtils.isNotEmpty(opLog.opBusinessId())) {
            SpelExpressionParser parser = new SpelExpressionParser();

            Expression expression = parser.parseExpression(opLog.opBusinessId());

            EvaluationContext context = new StandardEvaluationContext();

            // 获取参数值
            Object[] args = pjp.getArgs();
            // 获取运行时参数的名称
            LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

            String[] parameterNames = discoverer.getParameterNames(method);

            // 将参数绑定到context中
            if (parameterNames != null) {
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }
            }
            // 将方法的result当做变量放到context中，变量名称为该类名转化为小写字母开头的驼峰形式
            if (result != null) {
                context.setVariable(
                        CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, result.getClass().getSimpleName()),
                        result);
            }
            // 解析表达式，获取结果
            String itemId = String.valueOf(expression.getValue(context));
            // 执行日志记录
            Operation operation = Operation.builder()
                    .id(generatorId())
                    .opType(opLog.opType())
                    .opBusinessName(opLog.opBusinessName())
                    .opBusinessId(itemId)
                    .opTime(executeTime)
                    //这里可以添加操作人
                    .build();
            handle(operation);
        }
        return result;
    }

    private void handle(Operation operation) {
        // 通过日志打印输出,如果有需求可以创建一个operation_log表存入数据库
        log.info("opType = " + operation.getOpType().name() + ",opItem = " + operation.getOpBusinessName() + ",opItemId = " + operation.getOpBusinessId() + ",opTime = " + operation.getOpTime());
        // 持久化入库
        operationService.save(operation);
    }

    private String generatorId() {
        long l = snowflake.nextId();

        return String.valueOf(l);
    }
}
