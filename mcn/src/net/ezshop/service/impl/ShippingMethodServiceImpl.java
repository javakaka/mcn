package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.ShippingMethodDao;
import net.ezshop.entity.ShippingMethod;
import net.ezshop.service.ShippingMethodService;

import org.springframework.stereotype.Service;

/**
 * Service - 配送方式
 */
@Service("shippingMethodServiceImpl")
public class ShippingMethodServiceImpl extends BaseServiceImpl<ShippingMethod, Long> implements ShippingMethodService {

	@Resource(name = "shippingMethodDaoImpl")
	public void setBaseDao(ShippingMethodDao shippingMethodDao) {
		super.setBaseDao(shippingMethodDao);
	}

}