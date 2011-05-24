package com.m4f.web.bind.form;

import com.m4f.business.domain.CronTaskReport;


public class CronTaskReportFilterForm {
	
	private CronTaskReport.TYPE type;

	public CronTaskReport.TYPE getType() {
		return type;
	}

	public void setType(CronTaskReport.TYPE type) {
		this.type = type;
	}
}
