package com.m4f.business.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.CronTaskReport.TYPE;
import com.m4f.business.service.ifc.ICronTaskReportService;
import com.m4f.utils.dao.ifc.DAOSupport;

public class CronTaskReportServiceImpl extends DAOBaseService implements ICronTaskReportService {
		
	public CronTaskReportServiceImpl(DAOSupport dao) {
		super(dao);
	}

	@Override
	public long count() throws Exception {
		return this.DAO.count(CronTaskReport.class);
	}

	@Override
	public long countByType(TYPE type) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("type", type.getValue());
		return this.DAO.count(CronTaskReport.class, filter);
	}

	@Override
	public CronTaskReport create() {
		return this.DAO.createInstance(CronTaskReport.class);
	}

	@Override
	public void delete(long id) throws Exception {
		this.delete(this.DAO.findById(CronTaskReport.class, id));
	}

	@Override
	public void delete(CronTaskReport report) throws Exception {
		this.DAO.delete(report);
	}

	@Override
	public Collection<CronTaskReport> getAllCronTaskReport(String ordering) throws Exception {
		return this.DAO.findAll(CronTaskReport.class, ordering);
	}

	@Override
	public Collection<CronTaskReport> getAllCronTaskReport(String ordering,
			int init, int end) throws Exception {
		return this.DAO.findEntitiesByRange(CronTaskReport.class, ordering, init, end);
	}

	@Override
	public Collection<CronTaskReport> getCronTaskReportByType(TYPE type, String ordering)
			throws Exception {
		String filter = "type == typeParam";
		String params = "java.lang.String typeParam";
		return this.DAO.findEntities(CronTaskReport.class, filter, params, new Object[]{type.getValue()}, ordering);
	}

	@Override
	public Collection<CronTaskReport> getCronTaskReportByType(TYPE type,
			String ordering, int init, int end) {
		String filter = "type == typeParam";
		String params = "java.lang.String typeParam";
		return this.DAO.findEntitiesByRange(CronTaskReport.class, filter, params, new Object[]{type.getValue()}, ordering, init, end);
	}

	@Override
	public void save(CronTaskReport report) throws Exception {
		this.DAO.saveOrUpdate(report);
	}

	@Override
	public CronTaskReport getLastCronTaskReport(TYPE type) throws Exception {
		Collection<CronTaskReport> reports = this.DAO.findEntitiesByRange(CronTaskReport.class, "type == typeParam", "java.lang.String typeParam", new Object[]{type.getValue()},  "date desc", 0, 1);
		if(reports != null && !reports.isEmpty()) return (CronTaskReport) reports.toArray()[0];
		return new CronTaskReport();
	}	
}
