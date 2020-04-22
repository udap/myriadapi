package io.chainmind.myriadapi.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.vividsolutions.jts.geom.Point;

import io.chainmind.myriadapi.domain.OrganizationStatus;
import io.chainmind.myriadapi.domain.OrganizationType;
import io.chainmind.myriadapi.utils.AddressUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Organization extends UidEntity {
	private static final long serialVersionUID = 4991897248286153596L;
	
	@Enumerated(EnumType.STRING)
	private OrganizationType type;
	private String name;
	private String code;
	private String fullName;
	private String province;
	private String city;
	private String district;
	private String street;
	private String postalCode;
	private String phone;
	private String fax;
	private String licenseNo;
	
	@Enumerated(EnumType.STRING)
	private OrganizationStatus status = OrganizationStatus.NEW;
	
	// a private org is usually a subsidiary of an organization, 
	// or a private distribution point, or a private merchant of an org
	private boolean isPrivate;// 对其他机构是否可见

	@Column(name = "location", columnDefinition = "POINT")
	private Point location;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private Organization parent; // 创建机构

	@Transient
	public String getFullAddress() {
		return AddressUtils.buildFullAddress(province, city, district, street);
	}

	@Transient
	public boolean isEnabled() {
		return this.status.equals(OrganizationStatus.ACTIVE);
	}
	@Transient
	public boolean isDeleted() {
		return this.status.equals(OrganizationStatus.DELETED);
	}

	@Transient
	public boolean isActive() {
		return this.status.equals(OrganizationStatus.ACTIVE);
	}
}
