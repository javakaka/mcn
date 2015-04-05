package net.ezshop.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.ezshop.dao.UserDao;
import net.ezshop.entity.User;
import net.ezshop.service.UserService;

@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements
		UserService {
	
	@Resource(name = "userDaoImpl")
	public void setBaseDao(UserDao userDao) {
		super.setBaseDao(userDao);
	}
}
