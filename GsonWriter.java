package com.vgb;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonWriter {
	
	public static void convertPersonToGson(Map<UUID, Person> persons, String outputFilePath) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		
		String json = gson.toJson(persons);
		
		try (FileWriter w = new FileWriter(outputFilePath))
		{
			w.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void convertCompaniesToGson(List<Company> companyList, String outputFilePath) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		
		String json = gson.toJson(companyList);
		
		try (FileWriter w = new FileWriter(outputFilePath))
		{
			w.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void convertItemsToGson(List<Item> itemList, String outputFilePath) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		
		String json = gson.toJson(itemList);
		
		try (FileWriter w = new FileWriter(outputFilePath))
		{
			w.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
