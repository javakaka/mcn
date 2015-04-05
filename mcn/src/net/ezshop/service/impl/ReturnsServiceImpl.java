package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.ReturnsDao;
import net.ezshop.entity.Returns;
import net.ezshop.service.ReturnsService;

import org.springframework.stereotype.Service;

/**
 * Service - 退货单
 */
@Service("returnsServiceImpl")
public class ReturnsServiceImpl extends BaseServiceImpl<Returns, Long> implements ReturnsService {

	@Resource(name = "returnsDaoImpl")
	public void setBaseDao(ReturnsDao returnsDao) {
		super.setBaseDao(returnsDao);
	}

}