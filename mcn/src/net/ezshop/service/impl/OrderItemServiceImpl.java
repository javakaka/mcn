package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.OrderItemDao;
import net.ezshop.entity.OrderItem;
import net.ezshop.service.OrderItemService;

import org.springframework.stereotype.Service;

/**
 * Service - 订单项
 */
@Service("orderItemServiceImpl")
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long> implements OrderItemService {

	@Resource(name = "orderItemDaoImpl")
	public void setBaseDao(OrderItemDao orderItemDao) {
		super.setBaseDao(orderItemDao);
	}

}