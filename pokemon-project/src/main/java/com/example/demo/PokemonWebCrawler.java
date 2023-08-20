package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
public class PokemonWebCrawler {

	public static JsonArray run(String word) {
		String url = "https://pokemonhubs.com/pokemongo/26496/";

		try {
			// 使用 Jsoup 連接到網頁並取得 Document 物件
			Document document = Jsoup.connect(url).get();

			// 將 Document 的 HTML 寫入到檔案
			String htmlContent = document.outerHtml();
			String fileName = "parsed_html.html";
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
				writer.write(htmlContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// 從 Document 物件中解析所需的內容
//			String title = document.title();
//			System.out.println("標題: " + title);

			Elements tableRows = document.select("table tr");

			JsonArray jsonArray = new JsonArray();

			for (int i = 1; i < tableRows.size(); i++) {  // 從 1 開始以跳過表格的標題行
				Element row = tableRows.get(i);
				Elements columns = row.select("td");
				if(columns.size()>=2) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("Name", columns.get(0).text());
					jsonObject.addProperty("Type", columns.get(1).text());
					jsonArray.add(jsonObject);
				}
			}

			String jsonOutput = jsonArray.toString();
			// 儲存JSON檔案
			fileName = "data.json";
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
				writer.write(jsonOutput);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			System.out.println(jsonOutput);
			
			return jsonArray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
