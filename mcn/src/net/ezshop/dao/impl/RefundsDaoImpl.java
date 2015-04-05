package net.ezshop.dao.impl;

import net.ezshop.dao.RefundsDao;
import net.ezshop.entity.Refunds;

import org.springframework.stereotype.Repository;

/**
 * Dao - 退款单
 */
@Repository("refundsDaoImpl")
public class RefundsDaoImpl extends BaseDaoImpl<Refunds, Long> implements RefundsDao {

}