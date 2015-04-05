package net.ezshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 测试，用户
 * 
 * @author JianBoTong
 */
@Entity
@Table(name = "test_user")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "test_user_sequence")
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4606580137757776035L;

	private String name;
	private Integer age;

	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
	@Length(min = 6, max = 20)
	@Column(nullable = false, unique = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}