package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class PokemonWebCrawler {
	boolean fromOnline=false;
	String jsonFileName="data.json";
	// 爬蟲部分
	public JsonArray crawler() {
		String url = "https://pokemonhubs.com/pokemongo/26496/";

		try {
			// 使用 Jsoup 連接到網頁並取得 Document 物件
			Document document = Jsoup.connect(url).get();

			// 將 Document 的 HTML 寫入到檔案
			//			String htmlContent = document.outerHtml();
			//			String fileName = "parsed_html.html";
			//			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			//				writer.write(htmlContent);
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}

			// 從 Document 物件中解析所需的內容
			//			String title = document.title();
			//			System.out.println("標題: " + title);

			JsonArray jsonArray = new JsonArray();

			Elements tableElements = document.select("table");

			for (Element tableElement : tableElements) {
				Elements tableRows = tableElement.select("tr");

				for (int i = 1; i < tableRows.size(); i++) {  // 從 1 開始以跳過表格的標題行
					Element row = tableRows.get(i);
					Elements columns = row.select("td");
					if(columns.size()>=2) {

						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("Name", columns.get(0).text());
						jsonObject.addProperty("Type", columns.get(1).text());
						String image="https://lh3.googleusercontent.com/MxqAAjte5yQsQD0cC73SBVJuH6kX9F-fQ1X97VecyhGHGpixcT1ce7OftVuDufTSVIG_gY233437bOmw1AZFpg1Har4t5sP1jDo4GAX_so38dg=e365-w261";

						Document doc = Jsoup.parse(columns.get(1).html());
						Elements imgElements = doc.select("img");
						for (Element imgElement : imgElements) {
							String imageUrl = imgElement.attr("src");
							if(imageUrl.contains(".png")) {
								image=imageUrl;
								break;
							}
						}
						jsonObject.addProperty("Image", image);
						jsonArray.add(jsonObject);
					}
				}
			}
			String jsonOutput = jsonArray.toString();
			// 儲存JSON檔案
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFileName))) {
				writer.write(jsonOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jsonArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	public JsonArray run(String keyword) {
		keyword = keyword.trim();

		try {
			JsonArray jsonArray=new JsonArray();
			if (fromOnline) {
				jsonArray=this.crawler();
			}else {

				FileReader fileReader  = new FileReader(jsonFileName); 
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(fileReader);//讀檔案(已經爬蟲回來)
				jsonArray = jsonElement.getAsJsonArray();
			}
			
			
			// 篩選資料
			JsonArray filteredList = new JsonArray();
			for (JsonElement jsonElement : jsonArray) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				if(keyword != null &&(
						jsonObject.get("Name").toString().contains(keyword) ||
						jsonObject.get("Type").toString().contains(keyword))) {
					filteredList.add(jsonObject);
				}
			}
			// 沒有資料則全部
			if (filteredList.size() == 0) {
				return jsonArray;
			}
			return filteredList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
