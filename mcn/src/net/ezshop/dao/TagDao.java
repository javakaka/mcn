package net.ezshop.dao;

import java.util.List;

import net.ezshop.entity.Tag;
import net.ezshop.entity.Tag.Type;

/**
 * Dao - 标签
 */
public interface TagDao extends BaseDao<Tag, Long> {

	/**
	 * 查找标签
	 * 
	 * @param type
	 *            类型
	 * @return 标签
	 */
	List<Tag> findList(Type type);

}