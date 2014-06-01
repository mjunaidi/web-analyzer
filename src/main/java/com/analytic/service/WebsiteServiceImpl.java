package com.analytic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.analytic.dao.WebsiteDAO;
import com.analytic.model.Website;

@Service
@Transactional
public class WebsiteServiceImpl implements WebsiteService {
	
	@Autowired
	private WebsiteDAO websiteDAO;

	public void addWebsite(Website website) {
		websiteDAO.addWebsite(website);		
	}

	public void updateWebsite(Website website) {
		websiteDAO.updateWebsite(website);
	}

	public Website getWebsite(int id) {
		return websiteDAO.getWebsite(id);
	}

	public Website getWebsite(String name, String dateKey) {
		return websiteDAO.getWebsite(name, dateKey);
	}

	public void deleteWebsite(int id) {
		websiteDAO.deleteWebsite(id);
	}

	public List<Website> getWebsites() {
		return websiteDAO.getWebsites();
	}

	public List<String> getWebsiteNames() {
		return websiteDAO.getWebsiteNames();
	}

	public List<String> getDateKeys() {
		return websiteDAO.getDateKeys();
	}

	public List<Website> getWebsiteReport(String dateKey, int count) {
		return websiteDAO.getWebsiteReport(dateKey, count);
	}

	public List<Website> getWebsiteData(String name, int count) {
		return websiteDAO.getWebsiteData(name, count);
	}

	public List<String> getWebsiteNames(String name) {
		return websiteDAO.getWebsiteNames(name);
	}

	public List<String> getDateKeys(String dateKey) {
		return websiteDAO.getDateKeys(dateKey);
	}

}
