package net.ezshop.dao.impl;

import org.springframework.stereotype.Repository;

import net.ezshop.dao.UserDao;
import net.ezshop.entity.User;

@Repository("userDaoImpl")
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {

	

}
