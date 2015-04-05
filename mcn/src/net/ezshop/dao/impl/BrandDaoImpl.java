package net.ezshop.dao.impl;

import net.ezshop.dao.BrandDao;
import net.ezshop.entity.Brand;

import org.springframework.stereotype.Repository;

/**
 * Dao - 品牌
 */
@Repository("brandDaoImpl")
public class BrandDaoImpl extends BaseDaoImpl<Brand, Long> implements BrandDao {

}