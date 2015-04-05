package net.ezshop.service.impl;

import javax.annotation.Resource;

import net.ezshop.dao.LogDao;
import net.ezshop.entity.Log;
import net.ezshop.service.LogService;

import org.springframework.stereotype.Service;

/**
 * Service - 日志
 */
@Service("logServiceImpl")
public class LogServiceImpl extends BaseServiceImpl<Log, Long> implements LogService {

	@Resource(name = "logDaoImpl")
	private LogDao logDao;

	@Resource(name = "logDaoImpl")
	public void setBaseDao(LogDao logDao) {
		super.setBaseDao(logDao);
	}

	public void clear() {
		logDao.removeAll();
	}

}