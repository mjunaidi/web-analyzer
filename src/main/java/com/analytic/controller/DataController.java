package com.analytic.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.analytic.model.JsonData;
import com.analytic.service.JsonDataService;

@Controller
public class DataController {
	public final static String DATA_PATH = "/data";

	@Autowired
	private JsonDataService jsonDataService;

	@Autowired
	ServletContext context;
	
	@RequestMapping(value = "/datasource/{fileName}.{ext}", method = RequestMethod.GET)
	public ModelAndView getData(@PathVariable String fileName, @PathVariable String ext) {
		ModelAndView modelAndView = new ModelAndView("ajax");

		JsonData jsonData = jsonDataService.getJsonData(fileName + '.' + ext);

		if (jsonData != null) {
			modelAndView.addObject("response", jsonData.getData());
		} else {
			modelAndView.addObject("response", "");
		}
		return modelAndView;
	}

}
