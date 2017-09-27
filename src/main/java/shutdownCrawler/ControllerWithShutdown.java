package shutdownCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
 
/**
 * 爬虫控制器
 * @author user
 *
 */
public class ControllerWithShutdown {
 
  public static void main(String[] args) throws Exception {
   
    String crawlStorageFolder = "c:/crawl"; // 定义爬虫数据存储位置
 
    int numberOfCrawlers =2; // 定义2个爬虫，也就是2个线程
 
    CrawlConfig config = new CrawlConfig(); // 实例化爬虫配置
 
    config.setCrawlStorageFolder(crawlStorageFolder); // 设置爬虫文件存储位置
 
    config.setPolitenessDelay(1000); // 1秒爬一次
 
    config.setMaxPagesToFetch(-1); // 无限爬取
 
    // 实例化爬虫控制器
    PageFetcher pageFetcher = new PageFetcher(config); // 实例化页面获取器
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig(); // 实例化爬虫机器人配置 比如可以设置 user-agent
     
    // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    // 实例化爬虫控制器
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
 
    // 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
    controller.addSeed("http://www.java1234.com/");
    controller.addSeed("http://www.java1234.com/a/javaziliao/bishi/");
 
    // 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置 无阻塞
    controller.startNonBlocking(BasicCrawler.class, numberOfCrawlers);
 
    // 休息5秒
    Thread.sleep(10 * 1000);
     
    System.out.println("休息10秒");
 
    // 停止爬取
    controller.shutdown();
    System.out.println("停止爬取");
     
    // 等待结束任务
    controller.waitUntilFinish();
  }
}
