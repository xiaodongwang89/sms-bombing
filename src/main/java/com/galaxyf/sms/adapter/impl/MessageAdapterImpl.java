package com.galaxyf.sms.adapter.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.springframework.stereotype.Component;

import com.galaxyf.sms.adapter.IMessageAdapter;
import com.galaxyf.sms.config.Configuration;
import com.galaxyf.sms.dto.MessageResultDto;
import com.galaxyf.sms.utils.http.HttpClientUtil;
import com.galaxyf.sms.utils.http.common.HttpConfig;
import com.galaxyf.sms.utils.http.common.HttpHeader;
import com.galaxyf.sms.utils.http.exception.HttpProcessException;
import com.galaxyf.sms.utils.json.JSONUtil;
import com.google.common.base.Stopwatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright(c) 2017 by WayLau.
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author WayLau
 * @version <类版本>
 * @Package: com.galaxyf.crm.platform.adapter.impl
 * @date 2017/12/28  下午3:15
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */
@Slf4j
@Component("messageAdapter")
public class MessageAdapterImpl implements IMessageAdapter {

    @Resource
    private Configuration configuration;

    private static ExecutorService exec = new ThreadPoolExecutor(80, 80, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue(100000), new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public boolean sendMessage(String userMobile, String templetKey, String key, String content, String sessionId)
            throws HttpProcessException {
        String serverUrl = configuration.getMsgSendUrl();
        if (!StringUtils.isBlank(serverUrl)) {
            Map<String, Object> params = new HashMap<>();
            params.put("mobilenumber", userMobile);
            params.put("mCodeTepmlate", templetKey);
            params.put("paramInfo", key + ":" + content);
            params.put("sessionId", sessionId);
            Header[] headers = HttpHeader.custom().contentType("application/x-www-form-urlencoded").build();
            log.info("[{}]:: 请求报文<{}>", "sendMessage", JSONUtil.dumpObject(params));
            String result = "{\"msg\":\"发送成功\",\"code\":\"200\",\"data\":null,\"t\":\"2018-01-11 06:57:57\"}";
            if (configuration.isMsgEnable()) {
                result = HttpClientUtil
                        .post(HttpConfig.custom().headers(headers).encoding("UTF-8").url(serverUrl).map(params));
            }

            log.info("[{}]:: 响应报文<{}>", "sendMessage", result);
            if (!StringUtils.isBlank(result)) {
                MessageResultDto resultDto = JSONUtil.genBeanByJson(result, MessageResultDto.class);
                if ("200".equals(resultDto.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean sendMessage(String lineContent) throws HttpProcessException {
        String templateContent = configuration.getMsgTemplateContentPrimary();
        if (!StringUtils.isBlank(lineContent)) {
            List<String> fileList = Arrays.asList(lineContent.split(":"));
            if (null == fileList || fileList.size() < 1) {
                log.error("短信文件内容有误:" + lineContent);
                return false;
            }
            if ("2".equals(fileList.get(1))) {
                templateContent = configuration.getMsgTemplateContentSecondary();

            }
            String sessionId = "MICROSERVER_PALTFORM_" + UUID.randomUUID().toString().replace("-", "");
            boolean result = sendMessage(fileList.get(0), configuration.getMsgTemplate(), configuration.getMsgKey(),
                    templateContent, sessionId);
            return result;
        } else {
            return false;
        }

    }

    @Override
    public void sendMessageByFile() {
        List<Future<Boolean>> list = new ArrayList<>();
        Stopwatch sw = Stopwatch.createStarted();
        //StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        String line = null;
        // int lineNum = 1;
        try {
            //            reader = new BufferedReader(
            //                    new FileReader(new File(this.getClass().getClassLoader().getResource(fileName).getPath())));
            File file = new File(this.getClass().getClassLoader().getResource(configuration.getFileName()).getPath());
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            while (null != (line = reader.readLine())) {
                // result.append(line);
                //lineNum++;
                Future<Boolean> f = exec.submit(new RunCall(line));
                list.add(f);
            }
            reader.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        //        list.stream().forEach(e -> {
        //            try {
        //                System.out.println(e.get());
        //            } catch (InterruptedException e1) {
        //                System.err.println("1");
        //                e1.printStackTrace();
        //            } catch (ExecutionException e1) {
        //                System.err.println("2");
        //                e1.printStackTrace();
        //            }
        //        });
        log.info("end:" + sw.elapsed(TimeUnit.MILLISECONDS));

    }

    public class RunCall implements Callable<Boolean> {
        private String line;

        public RunCall(String line) {
            this.line = line;
        }

        @Override
        public Boolean call() throws Exception {
            Stopwatch sw = Stopwatch.createStarted();
            try {
                sendMessage(line);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                log.info(Thread.currentThread().getId() + "耗时:" + sw.elapsed(TimeUnit.MILLISECONDS));
            }
            return true;
        }

    }

}
