package com.analytic.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.analytic.model.JsonData;
import com.analytic.model.Website;
import com.analytic.service.JsonDataService;
import com.analytic.service.WebsiteService;
import com.analytic.util.JsonUtil;
import com.google.gson.JsonArray;

public class WebsiteBean {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DATE_KEY = "dateKey";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_WEBSITE = "website";
	public static final String COLUMN_VISITS = "visits";
	public static final String COLUMN_UPDATED = "updated";

	public final static String OUTPUT_PATH = "/data";
	public final static String WEBSITES_FILENAME = "websites.json";
	public final static String WEBSITE_NAMES_FILENAME = "websiteNames.json";
	public final static String DATE_KEYS_FILENAME = "dateKeys.json";

	public final static int TOP_COUNT = 100;
	public final static int WEEK_COUNT = 100;

	@Autowired
	private WebsiteService websiteService;

	@Autowired
	private JsonDataService jsonDataService;

	@Autowired
	private ServletContext context;

	public void consumeCsvData(List<Map<String, String>> csv) {
		if (csv != null && csv.size() > 0) {
			// to store list of newly created or updated websites
			List<Website> updatedWebsites = new ArrayList<>(csv.size());
			
			for (Map<String, String> map : csv) {
				Website website = createWebsiteFromMap(map);
				if (website != null) updatedWebsites.add(website);
			}
			generateWebsiteListJsonFile();
			generateWebsiteNamesJsonFile();
			generateDateKeysJsonFile();
			generateDatedReportJsonFiles(updatedWebsites);
			generateWebsiteJsonFiles(updatedWebsites);
		}
	}

	/**
	 * Generate list of websites.
	 */
	private void generateWebsiteListJsonFile() {
		List<Website> websites = websiteService.getWebsites();

		if (websites != null && websites.size() > 0) {

			JsonArray array = JsonUtil.INSTANCE
					.createJsonArrayFromWebsites(websites);

			if (array != null) {
				storeAsJsonData(WEBSITES_FILENAME, array);
			}
		}
	}

	/**
	 * Generate list of website names.
	 */
	private void generateWebsiteNamesJsonFile() {
		List<String> names = websiteService.getWebsiteNames();

		if (names != null && names.size() > 0) {

			JsonArray array = JsonUtil.INSTANCE.createJsonArrayFromList(names,
					"name");

			if (array != null) {
				storeAsJsonData(WEBSITE_NAMES_FILENAME, array);
			}
		}
	}

	/**
	 * Generate list of date keys.
	 */
	private void generateDateKeysJsonFile() {
		List<String> dateKeys = websiteService.getDateKeys();

		JsonArray array = JsonUtil.INSTANCE
				.createJsonArrayFromDateKeys(dateKeys);

		if (array != null) {
			storeAsJsonData(DATE_KEYS_FILENAME, array);
		}
	}

	/**
	 * Generate dated reports.
	 */
	private void generateDatedReportJsonFiles() {
		List<String> dateKeys = websiteService.getDateKeys();

		if (dateKeys != null && dateKeys.size() > 0) {
			for (String dateKey : dateKeys) {
				List<Website> websites = websiteService.getWebsiteReport(
						dateKey, TOP_COUNT);

				JsonArray array = JsonUtil.INSTANCE
						.createJsonArrayFromWebsites(websites);

				if (array != null) {
					String fileName = dateKey + ".json";
					storeAsJsonData(fileName, array);
				}
			}
		}
	}

	/**
	 * Generate dated reports for list of websites.
	 */
	private void generateDatedReportJsonFiles(List<Website> list) {
		List<String> dateKeys = getDateKeysFromList(list);

		if (dateKeys != null && dateKeys.size() > 0) {
			for (String dateKey : dateKeys) {
				List<Website> websites = websiteService.getWebsiteReport(
						dateKey, TOP_COUNT);

				JsonArray array = JsonUtil.INSTANCE
						.createJsonArrayFromWebsites(websites);

				if (array != null) {
					String fileName = dateKey + ".json";
					storeAsJsonData(fileName, array);
				}
			}
		}
	}
	
	private List<String> getDateKeysFromList(List<Website> websites) {
		if (websites != null && websites.size() > 0) {
			// get date keys from the website list
			List<String> dateKeys = new ArrayList<String>(websites.size());
			
			for (Website website : websites) {
				if (website != null) {
					String dateKey = website.getDateKey();
					if (dateKey != null && dateKey.length() > 0 && !dateKeys.contains(dateKey)) {
						dateKeys.add(dateKey);
					}
				}
			}
			return dateKeys;
		}
		return null;
	}

	/**
	 * Generate single website report for list of websites.
	 */
	private void generateWebsiteJsonFiles() {
		List<String> names = websiteService.getWebsiteNames();

		if (names != null && names.size() > 0) {
			for (String name : names) {
				List<Website> websites = websiteService.getWebsiteData(name,
						WEEK_COUNT);

				JsonArray array = JsonUtil.INSTANCE
						.createJsonArrayFromWebsites(websites);

				if (array != null) {
					String fileName = name + ".json";
					storeAsJsonData(fileName, array);
				}
			}
		}
	}

	/**
	 * Generate single website report.
	 */
	private void generateWebsiteJsonFiles(List<Website> list) {
		List<String> names = getWebsiteNamesFromList(list);

		if (names != null && names.size() > 0) {
			for (String name : names) {
				List<Website> websites = websiteService.getWebsiteData(name,
						WEEK_COUNT);

				JsonArray array = JsonUtil.INSTANCE
						.createJsonArrayFromWebsites(websites);

				if (array != null) {
					String fileName = name + ".json";
					storeAsJsonData(fileName, array);
				}
			}
		}
	}
	
	private List<String> getWebsiteNamesFromList(List<Website> websites) {
		if (websites != null && websites.size() > 0) {
			// get names from the website list
			List<String> names = new ArrayList<String>(websites.size());
			
			for (Website website : websites) {
				if (website != null) {
					String name = website.getName();
					if (name != null && name.length() > 0 && !names.contains(name)) {
						names.add(name);
					}
				}
			}
			return names;
		}
		return null;
	}

	private void storeAsJsonData(String name, JsonArray array) {
		JsonData jsonData = new JsonData();
		jsonData.setName(name);
		jsonData.setData(array.toString());
		saveOrUpdateJsonData(jsonData);
	}

	private void saveOrUpdateJsonData(JsonData jsonData) {
		JsonData existing = jsonDataService.getJsonData(jsonData.getName());
		if (existing == null) {
			jsonDataService.addJsonData(jsonData);
		} else {
			// if exist, then update
			if (!existing.getData().equals(jsonData.getData())) {
				existing.setData(jsonData.getData());
				jsonDataService.updateJsonData(existing);
			}
		}
	}

	private Website createWebsiteFromMap(Map<String, String> map) {
		if (map != null) {
			Website website = new Website();
			if (map.containsKey(COLUMN_DATE)) {
				website.setDateKey(map.get(COLUMN_DATE));
			}
			if (map.containsKey(COLUMN_WEBSITE)) {
				website.setName(map.get(COLUMN_WEBSITE));
			}
			if (map.containsKey(COLUMN_VISITS)) {
				String visitsStr = map.get(COLUMN_VISITS);
				Integer visits = Integer.parseInt(visitsStr);
				website.setVisits(visits);
			}
			return saveOrUpdateWebsite(website);
		}
		return null;
	}

	/**
	 * Returns newly created or updated website.
	 * @param website
	 * @return
	 */
	private Website saveOrUpdateWebsite(Website website) {
		Website existing = websiteService.getWebsite(website.getName(),
				website.getDateKey());
		if (existing == null) {
			websiteService.addWebsite(website);
			return website;
		} else {
			// if exist, then update
			if (!existing.getVisits().equals(website.getVisits())) {
				existing.setVisits(website.getVisits());
				websiteService.updateWebsite(existing);
				return existing;
			}
		}
		return null;
	}

	protected void deleteAllWebsites() {
		// delete all websites
		List<Website> websites = websiteService.getWebsites();
		if (websites != null && websites.size() > 0) {
			for (Website website : websites) {
				websiteService.deleteWebsite(website.getId());
			}
		}
	}
}
