package net.ezshop.dao.impl;

import net.ezshop.dao.AdPositionDao;
import net.ezshop.entity.AdPosition;

import org.springframework.stereotype.Repository;

/**
 * Dao - 广告位
 */
@Repository("adPositionDaoImpl")
public class AdPositionDaoImpl extends BaseDaoImpl<AdPosition, Long> implements AdPositionDao {

}