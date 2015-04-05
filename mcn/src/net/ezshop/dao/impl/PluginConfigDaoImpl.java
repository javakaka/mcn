package net.ezshop.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.ezshop.dao.PluginConfigDao;
import net.ezshop.entity.PluginConfig;

import org.springframework.stereotype.Repository;

/**
 * Dao - 插件配置
 */
@Repository("pluginConfigDaoImpl")
public class PluginConfigDaoImpl extends BaseDaoImpl<PluginConfig, Long> implements PluginConfigDao {

	public boolean pluginIdExists(String pluginId) {
		if (pluginId == null) {
			return false;
		}
		System.out.println("plugin id:"+pluginId);
		String jpql = "select count(*) from PluginConfig pluginConfig where pluginConfig.pluginId = :pluginId";
		System.out.println("jpql--->"+jpql);
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("pluginId", pluginId).getSingleResult();
		return count > 0;
	}

	public PluginConfig findByPluginId(String pluginId) {
		System.out.println("pluginid :"+pluginId);
		if (pluginId == null) {
			return null;
		}
		try {
			String jpql = "select pluginConfig from PluginConfig pluginConfig where pluginConfig.pluginId = :pluginId";
			System.out.println("jpql--->"+jpql);
			return entityManager.createQuery(jpql, PluginConfig.class).setFlushMode(FlushModeType.COMMIT).setParameter("pluginId", pluginId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}