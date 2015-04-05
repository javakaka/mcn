package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.ParameterGroupDao;
import net.ezshop.entity.ParameterGroup;
import net.ezshop.service.ParameterGroupService;

import org.springframework.stereotype.Service;

/**
 * Service - 参数组
 */
@Service("parameterGroupServiceImpl")
public class ParameterGroupServiceImpl extends BaseServiceImpl<ParameterGroup, Long> implements ParameterGroupService {

	@Resource(name = "parameterGroupDaoImpl")
	public void setBaseDao(ParameterGroupDao parameterGroupDao) {
		super.setBaseDao(parameterGroupDao);
	}

}