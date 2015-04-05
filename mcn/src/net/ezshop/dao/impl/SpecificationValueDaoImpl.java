package net.ezshop.dao.impl;

import net.ezshop.dao.SpecificationValueDao;
import net.ezshop.entity.SpecificationValue;

import org.springframework.stereotype.Repository;

/**
 * Dao - 规格值
 */
@Repository("specificationValueDaoImpl")
public class SpecificationValueDaoImpl extends BaseDaoImpl<SpecificationValue, Long> implements SpecificationValueDao {

}