package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.PaymentMethodDao;
import net.ezshop.entity.PaymentMethod;
import net.ezshop.service.PaymentMethodService;

import org.springframework.stereotype.Service;

/**
 * Service - 支付方式
 */
@Service("paymentMethodServiceImpl")
public class PaymentMethodServiceImpl extends BaseServiceImpl<PaymentMethod, Long> implements PaymentMethodService {

	@Resource(name = "paymentMethodDaoImpl")
	public void setBaseDao(PaymentMethodDao paymentMethodDao) {
		super.setBaseDao(paymentMethodDao);
	}

}