package com.m4f.business.service.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;

public interface I18nTerritorialService {
	Province createProvince();
	
	
	/*Province's methods. */
	List<Province> getAllProvinces(Locale locale) throws Exception;
	Map<Long, Province> getProvincesMap(Locale locale) throws Exception;
	Province getProvince(Long id, Locale locale) throws Exception;
	void save(Province province, Locale locale) throws Exception;
	void delete(Province province, Locale locale) throws Exception;
	Collection<Province> getProvinces(Locale locale, String ordering, int init, int end) throws Exception;	
	long countProvinces();
	
	/*Region's methods. */
	Region createRegion();
	List<Region> getAllRegions(Locale locale) throws Exception;
	Region getRegion(Long id, Locale locale) throws Exception;
	Map<Long, Region> getRegionsMap(Locale locale) throws Exception;
	List<Region> getRegionsByProvince(Long idProvince, Locale locale) throws Exception;
	void save(Region region, Locale locale) throws Exception;
	void delete(Region region, Locale locale) throws Exception;
	Collection<Region> getRegionsByProvince(Long idProvince, Locale locale, String ordering, int init, int end) throws Exception;	
	Collection<Region> getRegions(Locale locale, String ordering, int init, int end) throws Exception;	
	long countRegions();
	long countRegionsByProvince(Long provinceId);
	
	/* Town's methods. */
	Town createTown();
	void save(Town town, Locale locale) throws Exception;
	void delete(Town town, Locale locale) throws Exception;
	Town getTown(Long id, Locale locale) throws Exception;
	List<Town> getTowns(Locale locale, String ordering) throws Exception;
	Collection<Town> getTowns(Locale locale, String ordering, int init, int end) throws Exception;
	Collection<Town> getTownsByRegion(Long idRegion, Locale locale, String ordering) throws Exception;
	Collection<Town> getTownsByRegion(Long idRegion, Locale locale, String ordering, int init, int end) throws Exception;
	Collection<Town> getTownsByProvince(Long idRegion, Locale locale, String ordering, int init, int end) throws Exception;
	Collection<Town> getTownsByProvince(Long idProvince, Locale locale) throws Exception;
	List<Town> findTownsByName(String name, Locale locale);
	Map<String, Town> getTownsMap(Locale locale) throws Exception;
	
	long countTowns();
	long countTownsByRegion(Long regionId);
	long countTownsByProvince(Long regionId);
	
}