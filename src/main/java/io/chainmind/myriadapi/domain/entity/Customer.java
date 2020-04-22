package io.chainmind.myriadapi.domain.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

/**
 * 某个客户经理的客户
 */
@Entity
public class Customer extends UidEntity {
	private static final long serialVersionUID = -8067446483240027594L;
	public static final int TAG_MAX_Size = 10;
	
	private String name;
	private String coverImg;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_id")
	private Employee manager;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	private Organization org;

	private String remarks;

	private String tags;

	@Transient
	public Set<String> getTagSet() {
		if (StringUtils.hasText(this.getTags())){
			return new HashSet<String>(Arrays.asList(this.getTags().split(",")));
		}
		return Collections.emptySet();
	}
	@Transient
	public void addTag(String tag) {
		Set<String> tagSet = getTagSet();
		tagSet.add(tag);
		this.setTags(StringUtils.collectionToCommaDelimitedString(tagSet));
	}
	@Transient
	public void removeTag(String tag) {
		Set<String> tagSet = getTagSet();
		tagSet.remove(tag);
		this.setTags(StringUtils.collectionToCommaDelimitedString(tagSet));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
