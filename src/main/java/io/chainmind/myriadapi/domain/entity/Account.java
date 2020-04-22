package io.chainmind.myriadapi.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Account extends UidEntity {
	@Column(nullable = false)
	private String password;
	private String cellphone;
	private boolean enabled;
	private LocalDateTime expiryTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referer")
	private Account referer; // 邀请人，被谁邀请注册

	private String name;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Account getReferer() {
		return referer;
	}

	public void setReferer(Account referer) {
		this.referer = referer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
