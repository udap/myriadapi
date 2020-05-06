package io.chainmind.myriadapi.domain.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

@Entity
@Table(name = "org_closure")
@Immutable
public class OrganizationClosure implements Serializable {
	private static final long serialVersionUID = 165470968327399863L;

	@Id
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ancestor_id")
	private Organization ancestor;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "descendant_id")
	private Organization descendant;
	
	private int pathLength;

	public Organization getAncestor() {
		return ancestor;
	}

	public void setAncestor(Organization ancestor) {
		this.ancestor = ancestor;
	}

	public Organization getDescendant() {
		return descendant;
	}

	public void setDescendant(Organization descendant) {
		this.descendant = descendant;
	}

	public int getPathLength() {
		return pathLength;
	}

	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}
	
	
}
