package org.dolphin.study.crawler.netease.music.comments.downloader;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Command;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

public class NetEaseChromeDriverDownloader implements Downloader, Closeable {

    private volatile WebDriverPool webDriverPool;
    private Logger                 logger           = Logger.getLogger(this.getClass());
    private int                    sleepTime        = 0;
    private int                    poolSize         = 1;
    private static final String    DRIVER_PHANTOMJS = "phantomjs";

    public NetEaseChromeDriverDownloader(String chromeDriverPath){
        System.getProperties().setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    public NetEaseChromeDriverDownloader(){
    }

    public NetEaseChromeDriverDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public Page download(Request request, Task task) {
        this.checkInit();

        WebDriver webDriver;
        try {
            webDriver = this.webDriverPool.get();
        } catch (InterruptedException var10) {
            this.logger.warn("interrupted", var10);
            return null;
        }

        this.logger.info("downloading page " + request.getUrl());
        webDriver.get(request.getUrl());

        try {
            Thread.sleep((long) this.sleepTime);
        } catch (InterruptedException var9) {
            var9.printStackTrace();
        }

        WebDriver.Options manage = webDriver.manage();
        Site site = task.getSite();
        if (site.getCookies() != null) {
            Iterator webElement = site.getCookies().entrySet().iterator();

            while (webElement.hasNext()) {
                Map.Entry content = (Map.Entry) webElement.next();
                Cookie page = new Cookie((String) content.getKey(), (String) content.getValue());
                manage.addCookie(page);
            }
        }

        // 首页
        webDriver.switchTo().frame("contentFrame");
        WebElement webElement1 = webDriver.findElement(By.xpath("/html"));
        String content1 = webElement1.getAttribute("outerHTML");
        Page page1 = new Page();
        page1.setRawText(content1);
        page1.setHtml(new Html(content1, request.getUrl()));
        page1.setUrl(new PlainText(request.getUrl()));
        page1.setRequest(request);
        this.webDriverPool.returnToPool(webDriver);
        return page1;
    }

    private void checkInit() {
        if (this.webDriverPool == null) {
            synchronized (this) {
                this.webDriverPool = new WebDriverPool(this.poolSize);
            }
        }

    }

    public void setThread(int thread) {
        this.poolSize = thread;
    }

    public void close() throws IOException {
        this.webDriverPool.closeAll();
    }
}
