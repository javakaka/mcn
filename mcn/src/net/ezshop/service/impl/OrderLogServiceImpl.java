package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.OrderLogDao;
import net.ezshop.entity.OrderLog;
import net.ezshop.service.OrderLogService;

import org.springframework.stereotype.Service;

/**
 * Service - 订单日志
 */
@Service("orderLogServiceImpl")
public class OrderLogServiceImpl extends BaseServiceImpl<OrderLog, Long> implements OrderLogService {

	@Resource(name = "orderLogDaoImpl")
	public void setBaseDao(OrderLogDao orderLogDao) {
		super.setBaseDao(orderLogDao);
	}

}