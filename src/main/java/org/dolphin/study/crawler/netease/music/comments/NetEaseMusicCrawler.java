package org.dolphin.study.crawler.netease.music.comments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dolphin.study.crawler.netease.music.comments.downloader.NetEaseChromeDriverDownloader;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.net.URL;

/**
 * Created by qi.sun on 2018/10/30. 一个爬取网易云音乐评论的爬虫
 */
public class NetEaseMusicCrawler {

    private static Logger logger = LogManager.getLogger(NetEaseMusicCrawler.class);

    static {
        System.getProperties().setProperty("webdriver.chrome.driver", "/Users/sunqi/software/chromedriver");
        System.setProperty("webdriver.chrome.logfile", "/tmp/chromedriver.log");
        URL url = NetEaseMusicCrawler.class.getClassLoader().getResource("selinium/config.ini");
        System.getProperties().setProperty("selenuim_config", url.getFile());
    }

    public static void main(String[] args) {
        Spider.create(new NetEaseMusicCommontProcessor()).addUrl("https://music.163.com/").addRequest(new Request("https://music.163.com/")).setDownloader(new NetEaseChromeDriverDownloader("/Users/sunqi/software/chromedriver").setSleepTime(1000)).addPipeline(new ConsolePipeline()).run();
    }
}
