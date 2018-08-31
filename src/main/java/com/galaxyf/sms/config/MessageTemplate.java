package com.galaxyf.sms.config;

import java.util.Map;

import org.springframework.util.StringUtils;

public class MessageTemplate {

    public static final String MCODE_TEMPLATE = "您的验证码为【短信验证码】（10分钟有效），请尽快验证。如非本人操作，请致电【010-58037269】。";
    public static final String SZS_ZHENGXING_CODE_TEMPLATE = "尊敬的用户，根据石嘴山银行要求，您需要对人行征信查询授权书和人行征信查阅授权书（手动下载地址：$1）进行打印、签字、并邮寄到北京市海淀区信息路18号上地创新大厦四层  北京星河金服科技有限公司，邮编100085。为不影响您的正常放款，请尽快处理。";
    public static final String ACCESS_PASS = "【星河金融管家】尊敬的客户，您的贷款申请单【贷款申请单号】已进入初审，请您尽快登陆系统>我的贷款申请，提交补充资料。请保持电话畅通，感谢您的支持。【链接地址】";

    public static String replaceContent(String content, Map<String, String> map) {
        String mCode = map.get("mCode");
        String serviceTel = map.get("serviceTel");
        String path = map.get("path");
        String loanAppyName = map.get("loanAppyName");
        String repaymentTime = map.get("repaymentTime");
        if (content.contains("【短信验证码】")) {
            if (StringUtils.hasLength(mCode)) {
                content = content.replace("【短信验证码】", "【" + mCode + "】");
            }
        }
        if (content.contains("【服务商手机号】")) {
            if (StringUtils.hasLength(serviceTel)) {
                content = content.replace("【服务商手机号】", "【" + serviceTel + "】");
            }
        }

        if (content.contains("【链接地址】")) {
            if (StringUtils.hasLength(path)) {
                content = content.replace("【链接地址】", path);
            }
        }

        return content;
    }

}
