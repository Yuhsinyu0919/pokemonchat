package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.google.gson.JsonArray;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.message.Message;

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
//		System.out.println("event: " + event);

        PokemonWebCrawler crawler = new PokemonWebCrawler();
		JsonArray pokemonData = crawler.run(event.getMessage().getText().toString());
		return PokemonLineMessage.toCarouselTemplate(pokemonData);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}
}