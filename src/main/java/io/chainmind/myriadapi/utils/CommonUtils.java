package io.chainmind.myriadapi.utils;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class CommonUtils {	
	public final static String REGEX_PHONE = "^1[3456789][0-9]{9}$";
	public final static String REGEX_LP = "^[\u4e00-\u9fa5]{1}[A-Z_0-9]{6}$";
	public final static String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	public final static String REGEX_MD5 = "^[0-9a-fA-F]{32}$";
	public final static String REGEX_PHONE_CODE = "^[0-9]{4,6}$";
	public final static String REGEX_IMAGE = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";

	public static String maskCellphone(String cellphone) {
		return cellphone.replace(cellphone.substring(3, 7), "****");
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static boolean isImage(MultipartFile file) {
		if (file == null || file.isEmpty())
			return false;
		return file.getOriginalFilename().matches(REGEX_IMAGE);

	}

	public static boolean isImages(List<MultipartFile> files) {
		for (MultipartFile file : files) {
			if (!isImage(file))
				return false;
		}
		return true;

	}

	/**
	 * 验证<strong>手机号码</strong>格式是否合法
	 *
	 * @param phoneNumber 手机号码
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean validateCellphone(String phoneNumber) {
		// TODO: Consider international phones
		return phoneNumber == null ? false : phoneNumber.matches(REGEX_PHONE);
	}

	/**
	 * 验证<strong>车牌号</strong>格式是否合法
	 *
	 * @param phoneNumber 车牌号码
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean validateLicensePlate(String lpNumber) {
		return lpNumber == null ? false : lpNumber.matches(REGEX_LP);
	}

	/**
	 * 验证<strong>邮箱</strong>格式是否合法
	 *
	 * @param email 邮箱地址
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean validateEmail(String email) {
		return email == null ? false : email.matches(REGEX_EMAIL);
	}

	/**
	 * 验证<strong>md5密文</strong>格式是否合法
	 *
	 * @param md5 md5密文
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean validateMd5(String md5) {
		return md5 == null ? false : md5.matches(REGEX_MD5);
	}

	/**
	 * 验证<strong>手机验证码</strong>格式是否合法
	 *
	 * @param code 手机验证码
	 * @return 合法返回true，不合法返回false
	 */
	public static boolean validatePhoneCode(String code) {
		return code == null ? false : code.matches(REGEX_PHONE_CODE);
	}
	

}