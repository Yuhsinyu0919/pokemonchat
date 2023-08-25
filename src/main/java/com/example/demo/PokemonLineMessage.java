package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

public class PokemonLineMessage {

	public static Message toCarouselTemplate(JsonArray jsonArray) {
		List<CarouselColumn> columnList = new ArrayList<>();
		for (JsonElement jsonElement : jsonArray) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			String name = jsonObject.get("Name").getAsString();
			String type = jsonObject.get("Type").getAsString();
			String image = jsonObject.get("Image").getAsString();
			columnList.add( new CarouselColumn(
					image, name, type,
					Arrays.asList(
							new PostbackAction("詳情",  "command=reply", name)

							)
					));
			// line限制最多10則
			if(columnList.size()>9) {
				break;
			}

		}

		// 將 Carousel Column 放入 Carousel Template
		List<CarouselColumn> carouselColumns = columnList;
		CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);

		// 創建 TemplateMessage 並設置為 Carousel Template
		return new TemplateMessage("Carousel Template", carouselTemplate);
	}
}
