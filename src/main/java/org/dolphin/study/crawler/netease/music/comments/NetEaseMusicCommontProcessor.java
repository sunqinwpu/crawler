package org.dolphin.study.crawler.netease.music.comments;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * Created by qi.sun on 2018/10/30.
 */
public class NetEaseMusicCommontProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("music.163.com").setCharset("utf-8").setUserAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

    @Override public void process(Page page) {
        List<String> links = page.getHtml().links().regex("/song\\?id=\\d+").all();
        page.addTargetRequests(links);
        page.putField("comment", page.getHtml().xpath("//div[@class='cnt f-brk']").toString());

        List<Selectable> comments = page.getHtml().xpath("//div[@class='cnt f-brk']").nodes();
        if (!CollectionUtils.isEmpty(comments)) {
            for (Selectable comment : comments) {
                String userId = comment.css("a","href").toString();
                String userName = comment.css("a","text").toString();
                String commentContent = comment.css("div","text").toString();
                System.out.println(userId);
                System.out.println(userName);
                System.out.println(commentContent);
            }
        }


    }

    @Override public Site getSite() {
        return site;
    }
}
