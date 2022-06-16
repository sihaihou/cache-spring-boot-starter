package com.reyco.cache.core.handler.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public class ReycoCacheConfigurationSelector implements ImportSelector{

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		List<String> result = new ArrayList<>(3);
		result.add(AutoProxyRegistrar.class.getName());
		result.add(ReycoCachingConfiguration.class.getName());
		return StringUtils.toStringArray(result);
	}

}
