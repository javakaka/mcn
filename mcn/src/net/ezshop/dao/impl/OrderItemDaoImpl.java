package net.ezshop.dao.impl;

import net.ezshop.dao.OrderItemDao;
import net.ezshop.entity.OrderItem;

import org.springframework.stereotype.Repository;

/**
 * Dao - 订单项
 */
@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem, Long> implements OrderItemDao {

}