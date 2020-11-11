package io.chainmind.myriad.domain.common;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class Audience {
	@NotBlank
	private String id;
	private ServiceOrg serviceOrg;
	
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class ServiceOrg {
		// organization id
		@NotBlank
		String org;
		// comma separated ancestor orgs
		String ancestors;
		// manager id
		String manager;
		// tags assigned by the manager
		String tags;
		// customer ranking assigned by this organization
		String ranking;
	}
	
//	public Audience addFollowing(Following following) {
//		if (Objects.isNull(followings))
//			followings = new ArrayList<>();
//		followings.add(following);
//		return this;
//	}
}
