package com.zx;

import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author:ZhengXing
 * datetime:2017-12-28 20:05
 */
public class UserAgentParserTest {

    @Test
    public void test1() {
//        String a = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
        String a = "mukewang/5.0.0 (Android 5.1.1; Xiaomi Redmi 3 Build/LMY47V),Network 2G/3G";
        UserAgentParser userAgentParser  = new UserAgentParser();
        UserAgent agent = userAgentParser.parse(a);
        System.out.println(agent.getBrowser());
        System.out.println(agent.getEngine());
        System.out.println(agent.getEngineVersion());
        System.out.println(agent.getOs());
        System.out.println(agent.getPlatform());
        System.out.println(agent.getVersion());

        /**
         Chrome
         Webkit
         537.36
         Windows 7
         Windows
         54.0.2840.71
         */
    }


    /**
     * 直接按行读取日志文件中的所有数据,然后解析出UserAgent信息
     * 也可以把每一行的数据中的UserAgent字符串先提取出来,再用UserAgentParser解析
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        String file = "C:\\Users\\97038\\Desktop\\10000_access.log";
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(file)));
        List<String> list = IOUtils.readLines(in, "UTF-8");
        UserAgentParser userAgentParser  = new UserAgentParser();

        //计数器
        final AtomicInteger i = new AtomicInteger();

        Map<String, Long> totalizerMap = new HashMap<>();

        //使用该并发流需要线程安全
        //像这样写的话.每次统计结果都会不一样
        list.parallelStream().forEach(item->{
            UserAgent agent = userAgentParser.parse(item);
            //浏览器
            String browser = agent.getBrowser();
            System.out.println(browser + " , " + agent.getEngine() + " , " + agent.getEngineVersion() + " , " +
                    agent.getOs() + " , " + agent.getPlatform() + " , " + agent.getVersion());

            Long totalizer = totalizerMap.get(browser);
            if (totalizer != null) {
                totalizerMap.put(browser,totalizer + 1);
            }else{
                totalizerMap.put(browser, 1L);
            }
            i.incrementAndGet();
        });
        System.out.println(i.get());
        for (Map.Entry<String, Long> item : totalizerMap.entrySet()) {
            System.out.println(item.getKey() + " : " + item.getValue());
        }
    }

    /**
     * 测试  获取指定字符串中指定字符指定出现次数的索引位置 方法
     */
    @Test
    public void testGetCharacterPosition() {
        String a1 = "10.100.0.1 - - [10/Nov/2016:00:01:02 +0800] \"HEAD / HTTP/1.1\" 301 0 \"117.121.101.40\" \"-\" - \"curl/7.19.7 (x86_64-redhat-linux-gnu) libcurl/7.19.7 NSS/3.16.2.3 Basic ECC zlib/1.2.3 libidn/1.18 libssh2/1.4.2\" \"-\" - - - 0.000\n";
        String a2 = "182.106.215.93 - - [10/Nov/2016:00:01:02 +0800] \"POST /socket.io/1/ HTTP/1.1\" 200 94 \"chat.mukewang.com\" \"-\" - \"android-websockets-2.0\" \"-\" 10.100.15.239:80 200 0.004 0.004\n";

        //获取 字符串中, 第七个引号的 索引
        int index = getCharacterPosition(a2, "\"", 7);
        System.out.println(index);
    }

    /**
     * 获取指定字符串中指定字符指定出现次数的索引位置
     * @param value
     * @param operator
     * @param index
     * @return
     */
    private int getCharacterPosition(String value, String operator, int index) {
        Matcher matcher = Pattern.compile(operator).matcher(value);
        for(int mIdx = 0; matcher.find();mIdx++) {
            if (mIdx == index) {
                break;
            }
        }
        return matcher.start();
    }
}
