package io.chainmind.myriadapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity implements Serializable {
	private static final long serialVersionUID = -4411343024637985720L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@CreatedBy
    @Column(name = "created_by", nullable = false, length = 64, updatable = false)
    private String createdBy = "0";
	
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(updatable = false)
	private Date createTime;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updateTime;

	@LastModifiedBy
	@Column(name="updated_by",nullable=false,length=64)
	private String updatedBy = "0";

	public AuditableEntity() {
		super();
	}

	public AuditableEntity(Long id, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!this.getClass().isInstance(obj))
			return false;
		AuditableEntity target = (AuditableEntity) obj;
		// transient entities are always not equal
		// subclasses may override this method
		if (target.getId() == null || this.getId() == null)
			return false;
		return Objects.equals(this.getId(), target.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getId());
	}

}
