package com.m4f.business.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.business.service.ifc.I18nTerritorialService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class TerritorialService extends I18nDAOBaseService implements I18nTerritorialService {

	public TerritorialService(I18nDAOSupport dao) {
		super(dao);
	}
	
	
	/*Province's methods*/
	@Override
	public Province createProvince() {
		return (Province) this.DAO.createInstance(Province.class);
	}
	
	@Override
	@Cacheflush(cacheName="provinces")
	public void delete(Province province, Locale locale) throws Exception {
		this.DAO.delete(province, locale);
	}
		
	@Override
	@Cacheflush(cacheName="provinces")
	public void save(Province province, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(province, locale);
	}
	
	@Override
	@Cacheable(cacheName="provinces")
	public List<Province> getAllProvinces(Locale locale) throws Exception {
		return this.DAO.findAll(Province.class, locale, "name");
	}
	
	@Override
	@Cacheable(cacheName="provinces")
	public Map<Long, Province> getProvincesMap(Locale locale) throws Exception {
		List<Province> provinces = this.getAllProvinces(locale);
		Map<Long, Province> provincesMap = new HashMap<Long, Province>();
		for(Province province : provinces) {
			provincesMap.put(province.getId(), province);
		}
		return provincesMap;
	}

	@Override
	@Cacheable(cacheName="provinces")
	public Province getProvince(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Province.class, locale, id);
	}

	@Override
	@Cacheable(cacheName="provinces")
	public Collection<Province> getProvinces(Locale locale,
			String ordering, int init, int end) throws Exception {
		return this.DAO.findEntitiesByRange(Province.class, locale, init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="provinces")
	public long countProvinces() {
		return this.DAO.count(Province.class);
	}
	
	/* Region's operations */
	
	@Override
	public Region createRegion() {
		return (Region) this.DAO.createInstance(Region.class);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public Region getRegion(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Region.class, locale, id);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public List<Region> getAllRegions(Locale locale) throws Exception {
		return this.DAO.findAll(Region.class, locale, null);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public List<Region> getRegionsByProvince(Long idProvince, Locale locale)
			throws Exception {
		return (List<Region>) this.DAO.findEntities(Region.class, locale, 
				"province == provinceParam", 
				"Long provinceParam", new Object[]{ idProvince }, null);
	}
	
	@Override
	@Cacheflush(cacheName="regions")
	public void delete(Region region, Locale locale) throws Exception {
		this.DAO.delete(region, locale);
	}
	
	@Override
	@Cacheflush(cacheName="regions")
	public void save(Region region, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(region, locale);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public Map<Long, Region> getRegionsMap(Locale locale) throws Exception {
		Map<Long, Region> regionsMap = new HashMap<Long, Region>();
		List<Region> regions = this.getAllRegions(locale);
		for(Region region : regions) {
			regionsMap.put(region.getId(), region);
		}
		return regionsMap;
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public Collection<Region> getRegionsByProvince(Long idProvince, Locale locale,
			String ordering, int init, int end) throws Exception {
		return this.DAO.findEntitiesByRange(Region.class, locale, 
				"province == provinceParam", "Long provinceParam", new Object[]{ idProvince }, 
				init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public Collection<Region> getRegions(Locale locale,
			String ordering, int init, int end) throws Exception {
		return this.DAO.findEntitiesByRange(Region.class, locale, init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public long countRegions() {
		return this.DAO.count(Region.class);
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public long countRegionsByProvince(Long provinceId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("province", provinceId);
		return this.DAO.count(Region.class, filter);
	}
	
	/* Town's operations */
	@Override
	public Town createTown() {
		return (Town) this.DAO.createInstance(Town.class);
	}
	
	@Override
	@Cacheflush(cacheName="towns")
	public void delete(Town town, Locale locale) throws Exception {
		this.DAO.delete(town, locale);	
	}
	
	@Override
	@Cacheflush(cacheName="towns")
	public void save(Town town, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(town, locale);		
	}
	
	@Override
	@Cacheable(cacheName="regions")
	public Town getTown(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Town.class, locale, id);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public List<Town> findTownsByName(String name, Locale locale) {
		String hack = name + "\ufffd";
		return (List<Town>) this.DAO.findEntities(Town.class, locale, 
				"upperName >= nameParam && upperName < hackParam", 
				"String nameParam, String hackParam", 
				new Object[]{name.toUpperCase(), hack.toUpperCase()}, null);
	}

	@Override
	@Cacheable(cacheName="towns")
	public List<Town> getTowns(Locale locale, String ordering) throws Exception {
		return this.DAO.findAll(Town.class, locale, ordering);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public Collection<Town> getTownsByRegion(Long idRegion, 
			Locale locale, String ordering) throws Exception {
		return (List<Town>) this.DAO.findEntities(Town.class, locale, 
				"region == regionParam", "Long regionParam", 
				new Object[]{ idRegion }, ordering);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public Collection<Town> getTownsByProvince(Long idProvince, Locale locale)
			throws Exception {
		return this.DAO.findEntities(Town.class, locale, 
				"province == provinceParam", "Long provinceParam", 
				new Object[]{ idProvince }, null);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public Collection<Town> getTowns(Locale locale, String ordering, int init, int end)
			throws Exception {
		return this.DAO.findEntitiesByRange(Town.class, 
				locale,init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public Collection<Town> getTownsByRegion(Long idRegion, Locale locale,
			String ordering, int init, int end) throws Exception {
		return this.DAO.findEntitiesByRange(Town.class, locale, 
				"region == regionParam", "Long regionParam", new Object[]{ idRegion }, 
				init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public Collection<Town> getTownsByProvince(Long idProvince, Locale locale,
			String ordering, int init, int end) throws Exception {
		return this.DAO.findEntitiesByRange(Town.class, locale, 
				"province == provinceParam", "Long provinceParam", new Object[]{ idProvince }, 
				init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public long countTowns() {
		return this.DAO.count(Town.class);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public long countTownsByRegion(Long regionId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("region", regionId);
		return this.DAO.count(Town.class, filter);
	}
	
	@Override
	@Cacheable(cacheName="towns")
	public long countTownsByProvince(Long provinceId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("province", provinceId);
		return this.DAO.count(Town.class, filter);
	}
}