package com.analytic.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.analytic.model.JsonData;

@Repository
public class JsonDataDAOImpl implements JsonDataDAO {

	public static final int MAX = 100;

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void addJsonData(JsonData jsonData) {
		if (jsonData != null) {
			jsonData.setUpdated(new Date());
		}
		getCurrentSession().save(jsonData);
	}

	public void updateJsonData(JsonData jsonData) {
		JsonData jsonDataToUpdate = getJsonData(jsonData.getId());
		if (jsonDataToUpdate != null) {
			jsonDataToUpdate.setName(jsonData.getName());
			jsonDataToUpdate.setData(jsonData.getData());
			jsonDataToUpdate.setUpdated(new Date());
			getCurrentSession().update(jsonDataToUpdate);
		}
	}

	public JsonData getJsonData(int id) {
		JsonData jsonData = (JsonData) getCurrentSession().get(JsonData.class, id);
		return jsonData;
	}

	public JsonData getJsonData(String name) {
		if (name == null || name.length() <= 0)
			return null;
		JsonData jsonData = (JsonData) getCurrentSession()
				.createCriteria(JsonData.class)
				.add(Restrictions.eq("name", name)).uniqueResult();
		return jsonData;
	}

	public void deleteJsonData(int id) {
		JsonData jsonData = getJsonData(id);
		if (jsonData != null)
			getCurrentSession().delete(jsonData);
	}

	@SuppressWarnings("unchecked")
	public List<JsonData> getJsonDataList() {
		return getCurrentSession().createQuery("from JsonData").setMaxResults(MAX).list();
	}

}
