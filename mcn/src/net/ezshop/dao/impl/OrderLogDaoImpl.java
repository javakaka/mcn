package net.ezshop.dao.impl;

import net.ezshop.dao.OrderLogDao;
import net.ezshop.entity.OrderLog;

import org.springframework.stereotype.Repository;

/**
 * Dao - 订单日志
 */
@Repository("orderLogDaoImpl")
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog, Long> implements OrderLogDao {

}