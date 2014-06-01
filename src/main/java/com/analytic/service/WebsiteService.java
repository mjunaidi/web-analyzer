package com.analytic.service;

import java.util.List;

import com.analytic.model.Website;

public interface WebsiteService {
	
	public void addWebsite(Website website);
	public void updateWebsite(Website website);
	public Website getWebsite(int id);
	public Website getWebsite(String name, String dateKey);
	public void deleteWebsite(int id);
	public List<Website> getWebsites();
	public List<String> getWebsiteNames();
	public List<String> getDateKeys();
	public List<Website> getWebsiteReport(String dateKey, int count);
	public List<Website> getWebsiteData(String name, int count);
	public List<String> getWebsiteNames(String name);
	public List<String> getDateKeys(String dateKey);

}
