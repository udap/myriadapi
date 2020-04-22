package io.chainmind.myriadapi.domain.entity;

import javax.persistence.Entity;

/**
 * 商品类别
 */
@Entity
public class Category extends UidEntity {
	
	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
