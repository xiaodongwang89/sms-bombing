package com.galaxyf.sms.config;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright(c) 2017 by galaxy tech.
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 * @author WayLau
 * @version <类版本>
 * @date 2018年1月23日 下午5:27:18
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */
@Getter
@Setter
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${third.server.sms_service.send_msg_url}")
    private String msgSendUrl;
    @Value("${third.server.sms_service.enable}")
    private boolean msgEnable;
    @Value("${third.server.sms_service.msg_key}")
    private String msgKey;
    @Value("${third.server.sms_service.msg_template}")
    private String msgTemplate;
    @Value("${message.file.path}")
    private String fileName;
    private final String msgTemplateContentPrimary = "";
    private final String msgTemplateContentSecondary = "";
}
