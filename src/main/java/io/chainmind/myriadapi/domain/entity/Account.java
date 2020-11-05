package io.chainmind.myriadapi.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Account extends UidEntity {
	private static final long serialVersionUID = 7942902675046554817L;

	@Column(nullable = false)
	private String password;
	private String cellphone;
	private String email;
	private String sourceId;
	private boolean enabled;
	private LocalDateTime expiryTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referer")
	private Account referer; // 邀请人，被谁邀请注册

	private String name;
	// which org creates the account
	private String organizationId;

}
