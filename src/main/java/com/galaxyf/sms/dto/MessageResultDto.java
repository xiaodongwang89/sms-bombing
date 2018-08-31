package com.galaxyf.sms.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Copyright(c) 2017 by WayLau.
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author WayLau
 * @version <类版本>
 * @Package: com.galaxyf.sms.dto
 * @date 2017/12/28  下午5:10
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */
@Getter
@Setter
public class MessageResultDto {
    private String code; //返回码
    private String msg; //返回信息
    private String t;
    private Object data;
}
