/**
 * Copyright(c) 2017 by galaxy tech. 
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 * @author Administrator
 * @version <类版本>
 * @date 2018年1月24日 下午2:23:29
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */

package com.galaxyf.sms.task;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.galaxyf.sms.adapter.IMessageAdapter;
import com.google.common.base.Stopwatch;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private IMessageAdapter messageAdapter;

    @Scheduled(cron = "0 0 9,12,14 ? * FRI")
    public void executeSendMessage() {
        // 间隔1分钟,执行任务    
        Stopwatch sw = Stopwatch.createStarted();
        Thread current = Thread.currentThread();
        log.info("ScheduledTest.executeSendMessage 定时任务1:" + current.getId() + ",name:" + current.getName());
        messageAdapter.sendMessageByFile();
        log.info("end:" + sw.elapsed(TimeUnit.MILLISECONDS));
    }
}