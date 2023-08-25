package com.example.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;


@RestController
public class IndexApplication {
    @RequestMapping("/")
    public String sayHello(HttpServletRequest request) {
        String keyword = request.getParameter("keyword");
        PokemonWebCrawler crawler = new PokemonWebCrawler();
        JsonArray jsonArray = crawler.run(keyword);
        return jsonArray.toString();
    }
}