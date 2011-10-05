package com.m4f.business.service.ifc;

import java.util.Collection;

import com.m4f.business.domain.CronTaskReport;

public interface ICronTaskReportService {

    CronTaskReport create();
    Collection<CronTaskReport> getAllCronTaskReport(String ordering) throws Exception;
    Collection<CronTaskReport> getCronTaskReportByType(CronTaskReport.TYPE type, String ordering) throws Exception;
    long count() throws Exception;
    long countByType(CronTaskReport.TYPE type) throws Exception;
    CronTaskReport getLastCronTaskReport(CronTaskReport.TYPE type) throws Exception;
    Collection<CronTaskReport> getAllCronTaskReport(String ordering, int init, int end) throws Exception;
    Collection<CronTaskReport> getCronTaskReportByType(CronTaskReport.TYPE type, String ordering, int init, int end);
    void delete(long id) throws Exception;
    void delete(CronTaskReport report) throws Exception;
    void save(CronTaskReport report) throws Exception;
}
