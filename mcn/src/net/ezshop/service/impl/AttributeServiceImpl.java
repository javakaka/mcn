package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.AttributeDao;
import net.ezshop.entity.Attribute;
import net.ezshop.service.AttributeService;

import org.springframework.stereotype.Service;

/**
 * Service - 属性
 */
@Service("attributeServiceImpl")
public class AttributeServiceImpl extends BaseServiceImpl<Attribute, Long> implements AttributeService {

	@Resource(name = "attributeDaoImpl")
	public void setBaseDao(AttributeDao attributeDao) {
		super.setBaseDao(attributeDao);
	}

}