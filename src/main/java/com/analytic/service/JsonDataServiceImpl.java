package com.analytic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.analytic.dao.JsonDataDAO;
import com.analytic.model.JsonData;

@Service
@Transactional
public class JsonDataServiceImpl implements JsonDataService {

	@Autowired
	private JsonDataDAO jsonDataDAO;

	public void addJsonData(JsonData jsonData) {
		jsonDataDAO.addJsonData(jsonData);
	}

	public void updateJsonData(JsonData jsonData) {
		jsonDataDAO.updateJsonData(jsonData);
	}

	public JsonData getJsonData(int id) {
		return jsonDataDAO.getJsonData(id);
	}

	public JsonData getJsonData(String name) {
		return jsonDataDAO.getJsonData(name);
	}

	public void deleteJsonData(int id) {
		jsonDataDAO.deleteJsonData(id);
	}

	public List<JsonData> getJsonDataList() {
		return jsonDataDAO.getJsonDataList();
	}

	public void deleteAllJsonData() {
		List<JsonData> list = getJsonDataList();
		int max = 1000;
		int count = 0;
		while (list != null && list.size() > 0) {
			for (JsonData data : list) {
				deleteJsonData(data.getId());
			}
			list = getJsonDataList();
			count++;
			if (count >= max) {
				System.err.println("Somehow there is an error happened while deleting all websites.");
				break;
			}
		}
	}

}
