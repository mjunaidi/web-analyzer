package com.analytic.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.analytic.bean.WebsiteBean;
import com.analytic.model.Website;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum JsonUtil {

	INSTANCE;

	public static final String[] DEFAULT_KEYS = { "date", "website", "visits" };
	public static final String EMPTY_JSON = "{}";

	public JsonArray createJsonArrayFromWebsites(List<Website> websites) {
		if (websites != null && websites.size() > 0) {
			JsonArray array = new JsonArray();

			for (Website website : websites) {
				JsonObject json = new JsonObject();

				json.addProperty(WebsiteBean.COLUMN_ID, website.getId());
				json.addProperty(WebsiteBean.COLUMN_DATE_KEY,
						website.getDateKey());
				json.addProperty(WebsiteBean.COLUMN_DATE, website.getDate()
						.getTime());
				json.addProperty(WebsiteBean.COLUMN_NAME, website.getName());
				json.addProperty(WebsiteBean.COLUMN_VISITS, website.getVisits());
				json.addProperty(WebsiteBean.COLUMN_UPDATED, website
						.getUpdated().getTime());

				array.add(json);
			}
			return array;
		}
		return null;
	}

	public JsonArray createJsonArrayFromList(List<String> list, String propertyName) {
		if (list != null && list.size() > 0) {
			JsonArray array = new JsonArray();

			for (String entry: list) {
				JsonObject json = new JsonObject();

				json.addProperty(propertyName, entry);

				array.add(json);
			}
			
			return array;
		}
		return null;
	}

	public JsonArray createJsonArrayFromDateKeys(List<String> dateKeys) {
		if (dateKeys != null && dateKeys.size() > 0) {
			JsonArray array = new JsonArray();

			for (String dateKey: dateKeys) {
				JsonObject json = new JsonObject();

				json.addProperty("dateKey", dateKey);
				
				Date date = DateFormatterUtil.INSTANCE.getDateFromString(dateKey);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int day = cal.get(Calendar.DATE);
				int month = cal.get(Calendar.MONTH) + 1;
				String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
				int year = cal.get(Calendar.YEAR);

				String dayStr = day<10?"0"+day:String.valueOf(day);
				String monthStr = month<10?"0"+month:String.valueOf(month);
				
				json.addProperty("day", dayStr);
				json.addProperty("month", monthStr);
				json.addProperty("monthName", monthName);
				json.addProperty("year", year);

				array.add(json);
			}
			
			return array;
		}
		return null;
	}
}