package com.tracenull.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FileUpload {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUpload.class);

    @PostMapping("uploadFile")
    public List uploadFile(@RequestParam("files") MultipartFile[] files) {
        // 存储上传成功的文件名，响应给客户端
        List<String> list = new ArrayList<>();
        // 判断文件数组长度
        if (files.length <= 0) {
            list.add("请选择文件");
            return list;
        }

        for (MultipartFile file : files) {
            // 源文件名
            String originalFilename = file.getOriginalFilename();
            // 文件格式
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 新文件名，避免文件名重复，造成文件替换问题
            String fileName = UUID.randomUUID() + "." + suffix;
            // 文件存储路径
            String filePath = "/Users/ixingjue/IdeaProjects/springboot-learning/src/main/resources/img/";
            // 文件全路径
            File targetFile = new File(filePath + fileName);
            // 判断文件存储目录是否存在，不存在则新建目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdir();
            }

            // 将图片保存
            try {
                file.transferTo(targetFile);
                list.add(originalFilename);
            } catch (IOException e) {
                LOGGER.info("文件上传异常={}", e);
            }

        }
        return list;
    }
}
