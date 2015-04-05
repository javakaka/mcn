package net.ezshop.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import net.ezshop.dao.TagDao;
import net.ezshop.entity.Tag;
import net.ezshop.entity.Tag.Type;

import org.springframework.stereotype.Repository;

/**
 * Dao - 标签
 */
@Repository("tagDaoImpl")
public class TagDaoImpl extends BaseDaoImpl<Tag, Long> implements TagDao {

	public List<Tag> findList(Type type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
		Root<Tag> root = criteriaQuery.from(Tag.class);
		criteriaQuery.select(root);
		if (type != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.get("type"), type));
		}
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")));
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

}