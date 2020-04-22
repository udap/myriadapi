package io.chainmind.myriadapi.domain.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AuthorizedMerchant extends AuditableEntity {
	private static final long serialVersionUID = 3803902527327495621L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	private Organization org;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_id")	
	private Organization merchant;
	
	// union pay merchant code
	private String upCode;
	// wechat pay merchant code
	private String wpCode;
	// alipay merchant code
	private String apCode;
}
