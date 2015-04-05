package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.SpecificationDao;
import net.ezshop.entity.Specification;
import net.ezshop.service.SpecificationService;

import org.springframework.stereotype.Service;

/**
 * Service - 规格
 */
@Service("specificationServiceImpl")
public class SpecificationServiceImpl extends BaseServiceImpl<Specification, Long> implements SpecificationService {

	@Resource(name = "specificationDaoImpl")
	public void setBaseDao(SpecificationDao specificationDao) {
		super.setBaseDao(specificationDao);
	}

}