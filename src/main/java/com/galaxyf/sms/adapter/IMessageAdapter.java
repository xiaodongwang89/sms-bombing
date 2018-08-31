package com.galaxyf.sms.adapter;

import com.galaxyf.sms.utils.http.exception.HttpProcessException;

/**
 * Copyright(c) 2017 by WayLau.
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author WayLau
 * @version <类版本>
 * @Package: com.galaxyf.sms.adapter
 * @date 2017/12/28  下午3:15
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */
public interface IMessageAdapter {

    /**
     * 短信发送
     * @param userMobile 手机号码
     * @param templetKey 模版标识
     * @param key 内容key
     * @param content 内容
     * @param sessionId 唯一标识
     * @return
     */
    boolean sendMessage(String userMobile, String templetKey, String key, String content, String sessionId)
            throws HttpProcessException;

    /**
     * 短信发送
     * @param userMobile 手机号码
     * @return
     */
    boolean sendMessage(String userMobile) throws HttpProcessException;

    void sendMessageByFile();
}
