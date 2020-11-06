//package com.tracenull.controller;
//
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
///**
// * https://zebinh.github.io/2020/07/RocketMQDeploymentWithSpringBootDemo/
// */
//@RestController
//public class RocketController {
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    // 发送给Broker，默认会自动创建topic，topic和tag用冒号分隔
//    @GetMapping("/rocket/send")
//    public String rocketSend() {
//        LocalDateTime currentTime = LocalDateTime.now();
//        rocketMQTemplate.convertAndSend("rocket-topic-1", currentTime.toString());
//        return currentTime.toString();
//    }
//
//    // 延时消息，RocketMQ支持这几个级别的延时消息，不能自定义时长
//    // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
//    @GetMapping("/rocket/delayMsg/send")
//    public String rocketDelayMsgSend() {
//        LocalDateTime currentTime = LocalDateTime.now();
//
//        rocketMQTemplate.syncSend("rocket-topic-1:tag-2", MessageBuilder.withPayload(currentTime.toString()).build(), 2000, 3);
//
//        return currentTime.toString();
//    }
//
//
//}
