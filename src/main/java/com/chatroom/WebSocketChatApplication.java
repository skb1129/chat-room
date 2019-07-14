package com.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class WebSocketChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketChatApplication.class, args);
    }

    /**
     * Login Page
     */
    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    /**
     * ChatRoom Page
     * @param username: Username for chat room
     * @param session: HTTP session object
     */
    @PostMapping("/index")
    public ModelAndView index(String username, HttpSession session) throws UnknownHostException {
        session.setAttribute("username", username);
        return new ModelAndView("/chat");
    }
}
