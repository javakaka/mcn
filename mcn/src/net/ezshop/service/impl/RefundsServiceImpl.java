package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.RefundsDao;
import net.ezshop.entity.Refunds;
import net.ezshop.service.RefundsService;

import org.springframework.stereotype.Service;

/**
 * Service - 退款单
 */
@Service("refundsServiceImpl")
public class RefundsServiceImpl extends BaseServiceImpl<Refunds, Long> implements RefundsService {

	@Resource(name = "refundsDaoImpl")
	public void setBaseDao(RefundsDao refundsDao) {
		super.setBaseDao(refundsDao);
	}

}