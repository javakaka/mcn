package net.ezshop.dao.impl;

import net.ezshop.dao.ShippingMethodDao;
import net.ezshop.entity.ShippingMethod;

import org.springframework.stereotype.Repository;

/**
 * Dao - 配送方式
 */
@Repository("shippingMethodDaoImpl")
public class ShippingMethodDaoImpl extends BaseDaoImpl<ShippingMethod, Long> implements ShippingMethodDao {

}