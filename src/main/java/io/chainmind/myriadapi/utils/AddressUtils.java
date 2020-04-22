package io.chainmind.myriadapi.utils;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class AddressUtils {
	
	public static String buildFullAddress(String province, String city, String district, String street) {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.hasText(province))
			builder.append(province);
		if (StringUtils.hasText(city) && !Objects.equals(province, city))
			builder.append(city);
		if (StringUtils.hasText(district))
			builder.append(district);
		if (StringUtils.hasText(street))
			builder.append(street);
		return builder.toString();
	}
	
}
