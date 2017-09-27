package collectingData;
import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
 
/**
 * 这个类演示了crawler4j如何爬取一个网页的数据
 * 以及抽取出标题和文本信息
 */
public class Downloader {
 
  private final Parser parser;
  private final PageFetcher pageFetcher;
 
  public Downloader() {
    CrawlConfig config = new CrawlConfig(); // 实例化爬虫配置
    parser = new Parser(config); // 实例化解析器
    pageFetcher = new PageFetcher(config); // 实例化页面获取器
  }
 
  public static void main(String[] args) {
    Downloader downloader = new Downloader();
    downloader.processUrl("http://www.java1234.com/a/yuanchuang/j2sev2/2016/0606/6221.html");
    downloader.processUrl("http://blog.java1234.com/blog/articles/103.html");
  }
 
  public void processUrl(String url) {
    System.out.println("处理url:"+url);
    Page page = download(url);
    if (page != null) {
      ParseData parseData = page.getParseData(); // 获取解析数据
      if (parseData != null) {
        if (parseData instanceof HtmlParseData) { // 假如是html数据类型
          HtmlParseData htmlParseData = (HtmlParseData) parseData; // 获取数据
           
          System.out.println("标题: " + htmlParseData.getTitle());
          System.out.println("纯文本长度: " + htmlParseData.getText().length());
          System.out.println("html长度: " + htmlParseData.getHtml().length());
        }
      } else {
          System.out.println("不能解析该页面");
      }
    } else {
        System.out.println("不能获取页面的内容");
    }
  }
 
  /**
   * 下载指定Url的页面内容
   * @param url
   * @return
   */
  private Page download(String url) {
    WebURL curURL = new WebURL(); // 实例化weburl
    curURL.setURL(url); // 设置url
    PageFetchResult fetchResult = null;
    try {
      fetchResult = pageFetcher.fetchPage(curURL); // 获取爬取结果
      if (fetchResult.getStatusCode() == HttpStatus.SC_OK) { // 判断http状态是否是200 
        Page page = new Page(curURL); // 封装Page
        fetchResult.fetchContent(page); // 设置内容
        parser.parse(page, curURL.getURL()); // 解析page
        return page; // 返回page
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (fetchResult != null) { // 假如jvm没有回收 用代码回收对象 防止内存溢出
        fetchResult.discardContentIfNotConsumed(); // 销毁获取内容
      }
    }
    return null;
  }
}