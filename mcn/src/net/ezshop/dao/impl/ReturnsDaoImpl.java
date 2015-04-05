package net.ezshop.dao.impl;

import net.ezshop.dao.ReturnsDao;
import net.ezshop.entity.Returns;

import org.springframework.stereotype.Repository;

/**
 * Dao - 退货单
 */
@Repository("returnsDaoImpl")
public class ReturnsDaoImpl extends BaseDaoImpl<Returns, Long> implements ReturnsDao {

}