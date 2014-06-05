package com.analytic.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.analytic.model.Website;
import com.analytic.util.DateFormatterUtil;

@Repository
public class WebsiteDAOImpl implements WebsiteDAO {

	public static final int MAX = 100;

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	private Website convertDateKeyToDate(Website website) {
		if (website != null) {
			String dateKey = website.getDateKey();
			if (dateKey != null && dateKey.length() > 0) {
				website.setDate(DateFormatterUtil.INSTANCE
						.getDateFromString(dateKey));
			}
		}
		return website;
	}

	public void addWebsite(Website website) {
		if (website != null) {
			website = convertDateKeyToDate(website);
			website.setUpdated(new Date());
		}
		getCurrentSession().save(website);
	}

	public void updateWebsite(Website website) {
		Website websiteToUpdate = getWebsite(website.getId());
		if (websiteToUpdate != null) {
			websiteToUpdate.setName(website.getName());
			websiteToUpdate.setDateKey(website.getDateKey());
			websiteToUpdate.setVisits(website.getVisits());
			websiteToUpdate.setUpdated(new Date());

			websiteToUpdate = convertDateKeyToDate(websiteToUpdate);

			getCurrentSession().update(websiteToUpdate);
		}
	}

	public Website getWebsite(int id) {
		Website website = (Website) getCurrentSession().get(Website.class, id);
		return website;
	}

	public Website getWebsite(String name, String dateKey) {
		if (dateKey == null || name == null)
			return null;
		Website website = (Website) getCurrentSession()
				.createCriteria(Website.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("dateKey", dateKey)).uniqueResult();
		return website;
	}

	public void deleteWebsite(int id) {
		Website website = getWebsite(id);
		if (website != null)
			getCurrentSession().delete(website);
	}

	@SuppressWarnings("unchecked")
	public List<Website> getWebsites() {
		return getCurrentSession().createQuery("from Website").setMaxResults(MAX).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getWebsiteNames() {
		Criteria criteria = (Criteria) getCurrentSession()
				.createCriteria(Website.class)
				.setProjection(Projections.distinct(Projections.property("name")))
				.addOrder(Order.asc("name"))
				.setMaxResults(MAX);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getDateKeys() {
		Criteria criteria = (Criteria) getCurrentSession()
				.createCriteria(Website.class)
				.setProjection(Projections.distinct(Projections.property("dateKey")))
				.addOrder(Order.desc("dateKey"))
				.setMaxResults(MAX);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Website> getWebsiteReport(String dateKey, int count) {
		if (dateKey == null || count <= 0 || count > MAX)
			return null;
		List<Website> websites = getCurrentSession()
				.createCriteria(Website.class)
				.add(Restrictions.eq("dateKey", dateKey))
				.addOrder(Order.desc("visits"))
				.setMaxResults(count)
				.list();
		return websites;
	}

	@SuppressWarnings("unchecked")
	public List<Website> getWebsiteData(String name, int count) {
		if (name == null || name.length() <= 0 || count <= 0 || count > MAX)
			return null;
		List<Website> websites = getCurrentSession()
				.createCriteria(Website.class)
				.add(Restrictions.eq("name", name))
				.addOrder(Order.desc("dateKey"))
				.setMaxResults(count)
				.list();
		return websites;
	}

	@SuppressWarnings("unchecked")
	public List<String> getWebsiteNames(String name) {
		if (name == null || name.length() <= 0)
			return null;
		Criteria criteria = (Criteria) getCurrentSession()
				.createCriteria(Website.class)
				.setProjection(Projections.distinct(Projections.property("name")))
				.add(Restrictions.like("name", name))
				.addOrder(Order.asc("name"))
				.setMaxResults(MAX);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getDateKeys(String dateKey) {
		if (dateKey == null || dateKey.length() <= 0)
			return null;
		Criteria criteria = (Criteria) getCurrentSession()
				.createCriteria(Website.class)
				.setProjection(Projections.distinct(Projections.property("dateKey")))
				.add(Restrictions.like("dateKey", dateKey))
				.addOrder(Order.desc("dateKey"))
				.setMaxResults(MAX);
		return criteria.list();
	}

}
