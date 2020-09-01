package com.tracenull.mzitu;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 爬取妹子图片
 * https://mp.weixin.qq.com/s/3izE8VxBxwIKymh4BDbNlw
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@Slf4j
public class Meizitu {
    // 图片保存位置
    static String FileDir = "/Users/ixingjue/IdeaProjects/springboot-learning/mzitu/src/main/java/com/tracenull/mzitu/img/";
    static String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2595.400 QQBrowser/9.6.10872.400";
    static int queueMaxSize = 3;// 同时处理最大队列数量
    static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(queueMaxSize);
    static ExecutorService threadPool = new ThreadPoolExecutor(queueMaxSize, queueMaxSize, 0L, TimeUnit.MILLISECONDS, queue);

    // 目的：传递一个url（http的地址），返回对应地址下的HTML的文档
    public static String getHtml(String url) {

        HttpRequest createGet = HttpUtil.createGet(url);
        createGet.header("User-Agent", UserAgent)
                .timeout(10000);

        HttpResponse response;
        while (true) {
            try {
                response = createGet.execute();
                break;
            } catch (Exception e) {

            }
        }
        int status = response.getStatus();
        log.info("[{}] , 返回状态码:{}", url, status);
        if (status == 301 || status == 302) {
            HttpRequest createGet2 = HttpUtil.createGet(response.header("location"));
            createGet2.header("User-Agent", UserAgent)
                    .timeout(-1);
            HttpResponse response2 = createGet2.execute();
            log.info("[{}] , 返回状态码:{}", response.header("location"), response2.getStatus());
            return response2.body();
        }
        if (status == 404) {
            return "";
        }
        return response.body();
    }

    public static void downLoadImg(String url, String dir) {
        HttpRequest createGet = HttpUtil.createGet(url);
        createGet.header("User-Agent", UserAgent);
        createGet.header("Referer", "https://www.mzitu.com/");
        createGet.timeout(3000);
        HttpResponse response = createGet.execute();
        log.info("[{}],返回状态码：{}", url, response.getStatus());

        if (response.getStatus() == 429) {
            log.info("频率过快，两秒后重试");
            ThreadUtil.sleep(2000);
            downLoadImg(url, dir);
        }
        if (response.getStatus() == 404) {
            return;
        }
        String ext = url.substring(url.lastIndexOf("."));
        String img = "";
        img = UUID.randomUUID().toString() + ext;
        // 先建立文件夹
        File file = new File(FileDir + dir.replaceAll("/:", "").replaceAll("/?", "").replaceAll("\"", "") + "/");
        synchronized (file) {
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        response.writeBody(new File(FileDir + dir.replaceAll("/:", "").replaceAll("/?", "").replaceAll("\"", "") + "/" + img));
    }

    public static void submit(int index, int page, String url, String dir) {
        String url_cp = url;
        String dir_cp = dir;

        while (true) {
            ThreadUtil.sleep(20);
            if (queue.size() < queueMaxSize) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        while (true) {
                            try {
                                downLoadImg(url_cp, dir_cp);
                            } catch (Exception e) {
                                count++;
                                log.error("第" + index + "条,page:" + page + "下载失败" + count + "次", e);
                            }
                        }
                    }
                });
                return;
            }
        }
    }

    public static void main(String[] args) {
        // 初始化url
        String url = "https://www.mzitu.com/";
//        String url = "https://www.baidu.com";
        // 获取所有url
        String html = getHtml(url);
        // 解析url
        Document document = Jsoup.parse(html);
        Elements elements = document.select("[target=_blank]");
        log.info("数据共{}条", elements.size());
        int index = 0;
        for (Element element : elements) {
            index++;
            log.info("开始执行第{}条", index);
            String albumUrl = element.attr("href");
            // 遍历解析每一个URL,得到每一个相册的html
            String eachAlblumHtml = getHtml(albumUrl);
            Document eachAlblumDocument = Jsoup.parse(eachAlblumHtml);
            Elements ele_a = eachAlblumDocument.getElementsByClass("pagenavi").get(0).getElementsByTag("a");
            int pageSize = Integer.valueOf(ele_a.get(ele_a.size() - 2).text());
            log.info("最大页码:{}", pageSize);
            //找图片url
            Elements imgElements = eachAlblumDocument.select(".main-image img");
            if (imgElements.size() > 0) {
                String imgSrc = imgElements.get(0).attr("src");
                String baseImg = imgSrc.substring(0, imgSrc.length() - 6);

                for (int page = 1; page < pageSize; page++) {
                    int value = page;
                    try {
                        if (value < 10) {
                            submit(index, value, baseImg + "0" + value + ".jpg", element.text());
                        } else {
                            submit(index, value, baseImg + value + ".jpg", element.text());
                        }
                    } catch (Exception e) {
                        log.error("第" + index + "条,page:" + value + "下载失败######", e);
                        page--;
                    }
                }
                while (queue.size() != 0) {
                    ThreadUtil.sleep(20);
                }
            }
        }
    }
}