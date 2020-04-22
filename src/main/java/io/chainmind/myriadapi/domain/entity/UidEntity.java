package io.chainmind.myriadapi.domain.entity;

import io.chainmind.myriadapi.utils.CommonUtils;
import org.springframework.util.StringUtils;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.Date;

@MappedSuperclass
public class UidEntity extends AuditableEntity {
	private static final long serialVersionUID = 5818483202977919684L;
	
	private String uid;

	public UidEntity() {
		super();
	}

	public UidEntity(Long id, Date createTime, Date updateTime) {
		super(id, createTime, updateTime);
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@PrePersist
	public void prePersist() {
		if (!StringUtils.hasText(uid))
			uid = CommonUtils.generateUUID();
	}

}
