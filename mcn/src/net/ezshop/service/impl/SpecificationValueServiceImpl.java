package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.SpecificationValueDao;
import net.ezshop.entity.SpecificationValue;
import net.ezshop.service.SpecificationValueService;

import org.springframework.stereotype.Service;

/**
 * Service - 规格值
 */
@Service("specificationValueServiceImpl")
public class SpecificationValueServiceImpl extends BaseServiceImpl<SpecificationValue, Long> implements SpecificationValueService {

	@Resource(name = "specificationValueDaoImpl")
	public void setBaseDao(SpecificationValueDao specificationValueDao) {
		super.setBaseDao(specificationValueDao);
	}

}