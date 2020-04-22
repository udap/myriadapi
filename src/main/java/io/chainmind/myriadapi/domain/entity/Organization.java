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

	public OrganizationType getType() {
		return type;
	}

	public void setType(OrganizationType type) {
		this.type = type;
	}

	@Transient
	public String getFullAddress() {
		return AddressUtils.buildFullAddress(province, city, district, street);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public boolean getPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean aPrivate) {
		isPrivate = aPrivate;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	@Transient
	public boolean isEnabled() {
		return this.status.equals(OrganizationStatus.ACTIVE);
	}
	@Transient
	public boolean isDeleted() {
		return this.status.equals(OrganizationStatus.DELETED);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public OrganizationStatus getStatus() {
		return status;
	}

	public void setStatus(OrganizationStatus status) {
		this.status = status;
	}
	
	public boolean isActive() {
		return this.status.equals(OrganizationStatus.ACTIVE);
	}
}
