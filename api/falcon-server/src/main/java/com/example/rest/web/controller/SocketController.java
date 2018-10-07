package com.example.rest.web.controller;

import com.example.core.security.CurrentUser;
import com.example.core.security.UserPrincipal;
import com.example.core.model.socket.Message;
import com.example.core.model.socket.OutputMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class SocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final Logger log = LoggerFactory.getLogger(SocketController.class);

    @MessageMapping("/message")
    @SendToUser("/notification")
    public OutputMessage send(@Payload Message msg, UserPrincipal principal) throws Exception {
        OutputMessage out = new OutputMessage(msg.getUsername(), msg.getText(), new SimpleDateFormat("HH:mm").format(new Date()));
        return out;
    }

    @PostMapping("/send_push")
    public void push(@RequestBody Message message, @CurrentUser UserPrincipal principal) {
        simpMessagingTemplate.convertAndSendToUser(principal.getUsername(), "/notification", message);
    }
}
