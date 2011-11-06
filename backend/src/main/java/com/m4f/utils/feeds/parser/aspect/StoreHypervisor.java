package com.m4f.utils.feeds.parser.aspects;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.validation.FieldError;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;

@Aspect
public class StoreHypervisor {
	
	
	@AfterReturning(
	pointcut = "target(com.m4f.utils.feeds.parser.ifc.IStorage<School>) && args(objs,locale,provider)",
	argNames="objs, locale,provider,retVal", returning= "retVal")
	public void register(Collection<School> objs, Locale locale, Provider provider, Map<School, List<FieldError>> retVal) {
		System.out.println("Registering SchoolStoreHypervisor");
	}
	
	
}