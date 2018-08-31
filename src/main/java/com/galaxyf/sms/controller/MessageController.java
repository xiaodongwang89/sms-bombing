/**
 * Copyright(c) 2017 by galaxy tech. 
 * All Rights Reserved
 * <一句话功能简述>
 * <功能详细描述>
 * @author Administrator
 * @version <类版本>
 * @date 2018年1月24日 上午9:59:10
 * @see <相关类/方法>
 * @since <产品/模块版本>
 */

package com.galaxyf.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galaxyf.sms.adapter.IMessageAdapter;
import com.galaxyf.sms.dto.MessageResultDto;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageController {

    @Autowired
    private IMessageAdapter messageAdapter;

    @ApiOperation("短信发送")
    @PostMapping("/sendMsg")
    public MessageResultDto sendMsg() {
        MessageResultDto msg = new MessageResultDto();
        try {
            messageAdapter.sendMessageByFile();
            msg.setCode("success");
            msg.setMsg("成功");
            return msg;
        } catch (Exception e) {
            log.error(e.getMessage());
            msg.setCode("false");
            msg.setMsg(e.getMessage());
        }
        return msg;
    }
}
