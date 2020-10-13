package com.tracenull.controller;

import com.tracenull.utils.IdempotentUtils;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 重复提交
 * https://mp.weixin.qq.com/s/n9AFMwQB9V_fq_sED1EWvg
 */
@RequestMapping("/user")
@RestController
public class UserController {
    // 缓存id集合
    private Map<String, Integer> reqCache = new HashMap<>();

    /**
     * hashmap
     *
     * @param id
     * @return
     */
    @RequestMapping("/add")
    public String addUser(String id) {
        synchronized (this.getClass()) {
            // 重复请求判断
            if (reqCache.containsKey(id)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + id);
                return "执行失败";
            }
            // 存储请求id
            reqCache.put(id, 1);
        }
        System.out.println("添加用户ID:" + id);
        return "执行成功！";
    }

    private static String[] reqCache2 = new String[100];// 请求 ID 存储集合
    private static Integer reqCacheCounter = 0;// 请求计数器（指示 ID 存储的位置）

    /**
     * 优化版——固定大小的数组
     *
     * @param id
     * @return
     */
    @RequestMapping("/add2")
    public String addUser2(String id) {
        synchronized (this.getClass()) {
            if (Arrays.asList(reqCache2).contains(id)) {
                System.out.println("请勿重复提交！！！" + id);
                return "执行失败";
            }
        }
        // 记录请求 ID
        if (reqCacheCounter >= reqCache2.length) reqCacheCounter = 0;// 重置计数器
        reqCache2[reqCacheCounter] = id;
        reqCacheCounter++;// 下标往后移一位
        // 业务代码...
        System.out.println("添加用户ID:" + id);
        return "执行成功！";
    }

    private static String[] reqCache3 = new String[100];// 请求 ID 存储集合

    private static Integer reqCacheCounter2 = 0;// 请求计数器（指示 ID 存储的位置）

    /**
     * 双重检测锁(DCL)
     *
     * @param id
     * @return
     */
    @RequestMapping("/add3")
    public String addUser3(String id) {
        if (Arrays.asList(reqCache3).contains(id)) {
            // 重复请求
            System.out.println("请勿重复提交！！！" + id);
            return "执行失败";
        }
        synchronized (this.getClass()) {
            // 双重检查锁（DCL,double checked locking）提高程序的执行效率
            if (Arrays.asList(reqCache3).contains(id)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + id);
                return "执行失败";
            }
            // 记录请求 ID
            if (reqCacheCounter2 >= reqCache3.length) reqCacheCounter2 = 0; // 重置计数器
            reqCache3[reqCacheCounter2] = id; // 将 ID 保存到缓存
            reqCacheCounter2++; // 下标往后移一位
        }
        System.out.println("添加用户ID:" + id);
        return "执行成功！";
    }

    // 最大容量 100 个，根据 LRU 算法淘汰数据的 Map 集合
    private LRUMap<String, Integer> reqcache4 = new LRUMap<>(100);

    /**
     * Lru算法
     *
     * @param id
     * @return
     */
    @RequestMapping("/add4")
    public String addUser4(String id) {
        synchronized (this.getClass()) {
            if (reqcache4.containsKey(id)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + id);
                return "执行失败";
            }
            // 存储请求 ID
            reqcache4.put(id, 1);
        }
        // 业务代码...
        System.out.println("添加用户ID:" + id);
        return "执行成功！";
    }

    @RequestMapping("/add5")
    public String addUser5(String id) {
        // -------------- 幂等性调用（开始） --------------
        if (!IdempotentUtils.judge(id, this.getClass())) {
            return "执行失败";
        }
        // -------------- 幂等性调用（结束） --------------
        // 业务代码...
        System.out.println("添加用户ID:" + id);
        return "执行成功！";
    }
}
