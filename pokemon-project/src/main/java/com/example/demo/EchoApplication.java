package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    private final LineMessagingClient lineMessagingClient;

    @Autowired
    public EchoApplication(LineMessagingClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
    }
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);

        JsonArray pokemonData=PokemonWebCrawler.run("");
        List<CarouselColumn> columnList = new ArrayList<>();

        for (JsonElement jsonElement : pokemonData) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("Name").getAsString();
            String type = jsonObject.get("Type").getAsString();
            System.out.println(name);
//            actionList.add(new URIAction("捕抓5隻寶可夢", "https://www.google.com/"));
            columnList.add( new CarouselColumn(
           		 "https://i.imgur.com/9SWTaOS.jpeg",name,type,
                   Arrays.asList(
                           new URIAction("查看詳情", "https://www.google.com/"),
                           new PostbackAction("選項2", "action=option2")
                   )
           ));

            if(columnList.size()>9) {
            	break;
            }
            
        }

        // 將 Carousel Column 放入 Carousel Template
        List<CarouselColumn> carouselColumns = columnList;
        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);

        // 創建 TemplateMessage 並設置為 Carousel Template
        Message templateMessage = new TemplateMessage("Carousel Template", carouselTemplate);

        // 此時你可以將 templateMessage 發送給用戶
//	     lineMessagingClient.replyMessage(new ReplyMessage(event.getReplyToken(), templateMessage));
	     return templateMessage;
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}