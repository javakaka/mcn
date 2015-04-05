package net.ezshop.dao.impl;

import net.ezshop.dao.SpecificationDao;
import net.ezshop.entity.Specification;

import org.springframework.stereotype.Repository;

/**
 * Dao - 规格
 */
@Repository("specificationDaoImpl")
public class SpecificationDaoImpl extends BaseDaoImpl<Specification, Long> implements SpecificationDao {

}