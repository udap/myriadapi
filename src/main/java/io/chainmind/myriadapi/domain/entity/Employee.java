package io.chainmind.myriadapi.domain.entity;

import io.chainmind.myriadapi.domain.EmploymentStatus;
import io.chainmind.myriadapi.domain.Role;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Employee extends UidEntity {
	private static final long serialVersionUID = -2050040436261118127L;

	private String code;
	private String name;
	private String cellphone;
	private String desc;
	
	@Enumerated(EnumType.STRING)
	private Role role = Role.STAFF;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
//	@Where(clause = "deleted = false")
	private Organization org; // 哪个机构
	
	@Enumerated(EnumType.STRING)
	private EmploymentStatus status = EmploymentStatus.NEW;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date entryTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Deprecated
	@Transient
	public boolean getAdmin() {
		return isAdmin();
	}

	@Transient
	public boolean isAdmin() {
		return Role.ADMIN.equals(this.role);
	}
	
	public void setAdmin(boolean isAdmin) {
		this.role = isAdmin?Role.ADMIN:Role.STAFF;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public EmploymentStatus getStatus() {
		return status;
	}

	public void setStatus(EmploymentStatus status) {
		this.status = status;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Transient
	public boolean isDeleted() {
		return this.status.equals(EmploymentStatus.TERMINATED);
	}
	
	@Transient
	public boolean isActive() {
		return this.status.equals(EmploymentStatus.ACTIVE);
	}


}
