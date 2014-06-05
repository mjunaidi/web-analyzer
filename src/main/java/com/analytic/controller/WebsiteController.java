package com.analytic.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.analytic.bean.WebsiteBean;
import com.analytic.model.Website;
import com.analytic.service.JsonDataService;
import com.analytic.service.WebsiteService;
import com.analytic.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
@RequestMapping(value="/website")
public class WebsiteController {
	
	@Autowired
	private WebsiteService websiteService;

	@Autowired
	private JsonDataService jsonDataService;

	@Autowired
	private WebsiteBean websiteBean;
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView addWebsitePage() {
		ModelAndView modelAndView = new ModelAndView("add-website-form");
		Website website = new Website();
		website.setUpdated(new Date());
		modelAndView.addObject("website", website);
		return modelAndView;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public ModelAndView addingWebsite(@ModelAttribute Website website) {
		
		ModelAndView modelAndView = new ModelAndView("home");
		websiteService.addWebsite(website);
		
		String message = "Website was successfully added.";
		modelAndView.addObject("message", message);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/list")
	public ModelAndView listOfWebsites() {
		ModelAndView modelAndView = new ModelAndView("list-of-websites");
		
		List<Website> websites = websiteService.getWebsites();
		modelAndView.addObject("websites", websites);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public ModelAndView editWebsitePage(@PathVariable Integer id) {
		ModelAndView modelAndView = new ModelAndView("edit-website-form");
		Website website = websiteService.getWebsite(id);
		modelAndView.addObject("website",website);
		return modelAndView;
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.POST)
	public ModelAndView editingWebsite(@ModelAttribute Website website, @PathVariable Integer id) {
		
		ModelAndView modelAndView = new ModelAndView("home");
		
		websiteService.updateWebsite(website);
		
		String message = "Website was successfully edited.";
		modelAndView.addObject("message", message);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public ModelAndView deleteWebsite(@PathVariable Integer id) {
		ModelAndView modelAndView = new ModelAndView("home");
		websiteService.deleteWebsite(id);
		String message = "Website was successfully deleted.";
		modelAndView.addObject("message", message);
		return modelAndView;
	}
	
	@RequestMapping(value="/api/deleteAll", method=RequestMethod.GET)
	public ModelAndView deleteAllWebsitesApi() {
		ModelAndView modelAndView = new ModelAndView("ajax");
		websiteService.deleteAllWebsites();
		
		String message = "All websites were successfully deleted.";
        JsonObject json = new JsonObject();
        json.addProperty("message", message);
        modelAndView.addObject("response", json.toString());
		return modelAndView;
	}
	
	@RequestMapping(value = "/api/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteWebsiteApi(@PathVariable Integer id) {
		ModelAndView modelAndView = new ModelAndView("ajax");
		Website website = websiteService.getWebsite(id);

		String message;
		if (website != null) {
			websiteService.deleteWebsite(id);
			List<Website> list = new ArrayList<Website>(1);
			list.add(website);
			websiteBean.updateJsonFiles(list);
			message = "Website was successfully deleted.";
		} else {
			message = "Invalid website id!.";
		}
		JsonObject json = new JsonObject();
		json.addProperty("message", message);
		json.addProperty("id", id);
		modelAndView.addObject("response", json.toString());
		return modelAndView;
	}
	
	@RequestMapping(value="/api/search/name/{name}", method=RequestMethod.GET)
	public ModelAndView searchWebsiteName(@PathVariable String name) {
		ModelAndView modelAndView = new ModelAndView("ajax");
		List<String> names = websiteService.getWebsiteNames(name);
		
		JsonArray array = JsonUtil.INSTANCE.createJsonArrayFromList(names, "name");
		
		if (array != null) {
			modelAndView.addObject("response", array.toString());
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value="/api/search/dateKey/{dateKey}", method=RequestMethod.GET)
	public ModelAndView searchWebsiteDateKey(@PathVariable String dateKey) {
		ModelAndView modelAndView = new ModelAndView("ajax");
		List<String> dateKeys = websiteService.getDateKeys(dateKey);
		
		JsonArray array = JsonUtil.INSTANCE.createJsonArrayFromDateKeys(dateKeys);
		
		if (array != null) {
			modelAndView.addObject("response", array.toString());
		}
		
		return modelAndView;
	}

}
