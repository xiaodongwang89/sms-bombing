package com.galaxyf.sms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.galaxyf.sms.adapter.IMessageAdapter;
import com.google.common.base.Stopwatch;

/**
 * @author zq
 * @version V1.0
 * @Title: BankTest
 * @Package com.xhjf
 * @Description:
 * @date 2017/10/9 13:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SendMsgTest {

    private static ExecutorService exec = new ThreadPoolExecutor(80, 80, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue(100000), new ThreadPoolExecutor.CallerRunsPolicy());

    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    public static final String SZS_ZHENGXING_CODE_TEMPLATE = "尊敬的用户，根据石嘴山银行要求，您需要对人行征信查询授权书和人行征信查阅授权书（手动下载地址：$1）进行打印、签字、并邮寄到北京市海淀区信息路18号上地创新大厦四层  北京星河金服科技有限公司，邮编100085。为不影响您的正常放款，请尽快处理。";
    @Value("${message.file.path}")
    private String fileName;
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(60000).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(100);
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).setConnectionManager(cm).build();
    }

    @Autowired
    private IMessageAdapter messageAdapter;

    @Test
    public void testSendMsgByFile() {
        messageAdapter.sendMessageByFile();
    }

    @Test
    public void testSendMsgBatch() {
        //         BufferedInputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(fileName));
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(new File(this.getClass().getClassLoader().getResource(fileName).getPath())));
            String line = null;
            while (null != (line = br.readLine())) {
                System.out.println(line);

            }
        } catch (FileNotFoundException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        List<Future<Boolean>> list = new ArrayList<>();
        Stopwatch sw = Stopwatch.createStarted();

        for (int i = 1; i <= 1; i++) {
            Future<Boolean> f = exec.submit(new RunCall());
            list.add(f);
        }
        list.stream().forEach(e -> {
            try {
                System.out.println(e.get());
            } catch (InterruptedException e1) {
                System.err.println("1");
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                System.err.println("2");
                e1.printStackTrace();
            }
        });
        System.err.println("end:" + sw.elapsed(TimeUnit.MILLISECONDS));

    }

    public class RunCall implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            Stopwatch sw = Stopwatch.createStarted();
            try {
                sendMsg();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                System.err.println(Thread.currentThread().getId() + "耗时:" + sw.elapsed(TimeUnit.MILLISECONDS));
            }
            return true;
        }
    }

    private void sendMsg() {

        try {

            System.out.println("Thread.currentThread().getId(): " + Thread.currentThread().getId());
            String msg_content = SZS_ZHENGXING_CODE_TEMPLATE.replace("$1",
                    "http://m.xhjrgj.com/fileDownload/downloadSzszxsqs");

            sendMessage("", msg_content, "ddd");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void sendMessage(String userMobile, String content, String sessionId) {
        String templetKey = "BUSINESS_OPERATION_KEY";
        String key = "MESSAGE_CONTENT";

        Map<String, String> map = new HashMap<>();
        map.put("mobilenumber", userMobile);
        map.put("mCodeTepmlate", templetKey);
        map.put("paramInfo", key + ":" + content);
        map.put("sessionId", sessionId);

        String status = doPost("http://10.11.18.36:8080/message/mCode/sendMsg.do", map, CHARSET);
        System.out.println("发送短信结果:" + status);

    }

    /**
     * HTTP Post 获取内容
     * 
     * @param url
     *            请求的url地址 ?之前的地址
     * @param params
     *            请求的参数
     * @param charset
     *            编码格式
     * @return 页面内容
     */
    public static String doPost(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, charset);
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HttpClient,error status code ");
        }
    }

}
