package com.m4f.utils.feeds.parser.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.validation.FieldError;
import com.m4f.business.domain.BaseEntity;
import com.m4f.business.domain.Provider;

public interface IStorage<T extends BaseEntity> {
	Map<T, List<FieldError>> store(Collection<T> objs, Locale locale, Provider provider) throws Exception;
}