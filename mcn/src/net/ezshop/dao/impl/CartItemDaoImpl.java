package net.ezshop.dao.impl;

import net.ezshop.dao.CartItemDao;
import net.ezshop.entity.CartItem;

import org.springframework.stereotype.Repository;

/**
 * Dao - 购物车项
 */
@Repository("cartItemDaoImpl")
public class CartItemDaoImpl extends BaseDaoImpl<CartItem, Long> implements CartItemDao {

}