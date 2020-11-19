package com.tracenull.service;

import com.tracenull.controller.EmailController;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EmailService {
    @Autowired
    private ApplicationContext applicationContext;

    @Async
    public void testSyncTask() throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("异步任务执行完成！");
    }

    public void asyncCallTwo() throws InterruptedException {
        //this.testSyncTask();
//        EmailService emailService = (EmailService)applicationContext.getBean(EmailService.class);
//        emailService.testSyncTask();
        boolean isAop = AopUtils.isAopProxy(EmailController.class);//是否是代理对象；
        boolean isCglib = AopUtils.isCglibProxy(EmailController.class);  //是否是CGLIB方式的代理对象；
        boolean isJdk = AopUtils.isJdkDynamicProxy(EmailController.class);  //是否是JDK动态代理方式的代理对象；
        //以下才是重点!!!
        EmailService emailService = (EmailService) applicationContext.getBean(EmailService.class);
        EmailService proxy = (EmailService) AopContext.currentProxy();
        System.out.println(emailService == proxy ? true : false);
        proxy.testSyncTask();
        System.out.println("end!!!");
    }
}
