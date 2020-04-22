package io.chainmind.myriadapi.domain.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AppRegistration extends AuditableEntity {
	private static final long serialVersionUID = 7757686587112912041L;

	private String appName;

	private String homepageUrl;
	private String description;
	private String authorizationCallbackUrl;
	private String appId;
	private String appSecret;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	private Organization org;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHomepageUrl() {
		return homepageUrl;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthorizationCallbackUrl() {
		return authorizationCallbackUrl;
	}

	public void setAuthorizationCallbackUrl(String authorizationCallbackUrl) {
		this.authorizationCallbackUrl = authorizationCallbackUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
}
