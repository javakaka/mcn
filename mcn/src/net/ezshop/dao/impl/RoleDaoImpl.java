package net.ezshop.dao.impl;

import net.ezshop.dao.RoleDao;
import net.ezshop.entity.Role;

import org.springframework.stereotype.Repository;

/**
 * Dao - 角色
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {

}