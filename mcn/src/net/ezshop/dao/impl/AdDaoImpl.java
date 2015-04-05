package net.ezshop.dao.impl;

import net.ezshop.dao.AdDao;
import net.ezshop.entity.Ad;

import org.springframework.stereotype.Repository;

/**
 * Dao - 广告
 */
@Repository("adDaoImpl")
public class AdDaoImpl extends BaseDaoImpl<Ad, Long> implements AdDao {

}