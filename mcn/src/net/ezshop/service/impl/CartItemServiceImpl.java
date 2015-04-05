package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.CartItemDao;
import net.ezshop.entity.CartItem;
import net.ezshop.service.CartItemService;

import org.springframework.stereotype.Service;

/**
 * Service - 购物车项
 */
@Service("cartItemServiceImpl")
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, Long> implements CartItemService {

	@Resource(name = "cartItemDaoImpl")
	public void setBaseDao(CartItemDao cartItemDao) {
		super.setBaseDao(cartItemDao);
	}

}