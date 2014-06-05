package com.analytic.service;

import java.util.List;

import com.analytic.model.JsonData;

public interface JsonDataService {
	
	public void addJsonData(JsonData jsonData);
	public void updateJsonData(JsonData jsonData);
	public JsonData getJsonData(int id);
	public JsonData getJsonData(String name);
	public void deleteJsonData(int id);
	public List<JsonData> getJsonDataList();
	public void deleteAllJsonData();
}
