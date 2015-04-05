package net.ezshop.dao.impl;

import net.ezshop.dao.DeliveryCorpDao;
import net.ezshop.entity.DeliveryCorp;

import org.springframework.stereotype.Repository;

/**
 * Dao - 物流公司
 */
@Repository("deliveryCorpDaoImpl")
public class DeliveryCorpDaoImpl extends BaseDaoImpl<DeliveryCorp, Long> implements DeliveryCorpDao {

}