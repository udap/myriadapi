package io.chainmind.myriadapi.domain.dto;

import java.io.Serializable;
import java.util.Objects;

public class NamedEntityDTO implements Serializable {
	private static final long serialVersionUID = 1665089400939714964L;

	private Long id;
	private String uid;
	private String name;

	private String desc;

	public NamedEntityDTO() {
	}

	public NamedEntityDTO(String name) {
		this.name = name;
	}
	
	public NamedEntityDTO(Long id, String uid, String name) {
		this.id = id;
		this.uid = uid;
		this.name = name;
	}
	
	public NamedEntityDTO(Long id, String name) {
		this.id = id;
		this.name = name;		
	}
	
	public NamedEntityDTO(String uid, String name) {
		this.uid = uid;
		this.name = name;				
	}

	public static NamedEntityDTO build(String uid, String name) {
		NamedEntityDTO e = new NamedEntityDTO(name);
		e.setUid(uid);
		return e;
	}

	public static NamedEntityDTO build(Long id, String name) {
		NamedEntityDTO e = new NamedEntityDTO(name);
		e.setId(id);
		return e;
	}

	public static NamedEntityDTO build(Long id, String uid, String name) {
		NamedEntityDTO e = new NamedEntityDTO(name);
		e.setId(id);
		e.setUid(uid);
		return e;
	}

	public Long getId() {
		return id;
	}

	public NamedEntityDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUid() {
		return uid;
	}

	public NamedEntityDTO setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public String getName() {
		return name;
	}

	public NamedEntityDTO setName(String name) {
		this.name = name;
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public NamedEntityDTO setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid, name, desc);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof NamedEntityDTO))
			return false;
		NamedEntityDTO ne = (NamedEntityDTO) o;
		return Objects.deepEquals(this.id, ne.getId()) && Objects.equals(this.uid, ne.getUid())
				&& Objects.equals(this.name, ne.getName()) && Objects.equals(this.desc, ne.desc);

	}

}
