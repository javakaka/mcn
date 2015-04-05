package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.ParameterDao;
import net.ezshop.entity.Parameter;
import net.ezshop.service.ParameterService;

import org.springframework.stereotype.Service;

/**
 * Service - 参数
 */
@Service("parameterServiceImpl")
public class ParameterServiceImpl extends BaseServiceImpl<Parameter, Long> implements ParameterService {

	@Resource(name = "parameterDaoImpl")
	public void setBaseDao(ParameterDao parameterDao) {
		super.setBaseDao(parameterDao);
	}

}