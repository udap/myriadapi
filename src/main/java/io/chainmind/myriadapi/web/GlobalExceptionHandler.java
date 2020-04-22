package io.chainmind.myriadapi.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import io.chainmind.myriadapi.domain.exception.ApiException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/*@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;
	@Value("${spring.servlet.multipart.max-request-size}")
	private String maxRequestSize;*/


	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> defaultErrorHandler(Exception ex) throws Exception {
		logger.error("异常了------",ex);
		ResponseEntity<String> stringResponseEntity =
				new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return stringResponseEntity;
	}


	@ExceptionHandler(ApiException.class)
	@ResponseBody
	public ResponseEntity<Object> apiErrorHandler(ApiException apiException) throws Exception {
//		logger.debug("ApiException [ {} ]", apiException.getMessage());
		return new ResponseEntity<Object>(apiException.getBody(), apiException.getStatus());
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<String> argumentErrorHandler(MethodArgumentNotValidException ex)
			throws Exception {
		logger.debug("异常了------", ex);
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public ResponseEntity<String> bindException(HttpServletRequest req, BindException ex) throws Exception {
		logger.debug("异常了------", ex);
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public ResponseEntity<String> methodArgumentTypeMismatch(HttpServletRequest req,
															 MethodArgumentTypeMismatchException exception) throws Exception {
		logger.debug("异常了------", exception);
		return new ResponseEntity<String>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = MultipartException.class)
	@ResponseBody
	public ResponseEntity<String> multipartErrorHandler(HttpServletRequest req, MultipartException ex) throws Exception {
//		Result<Object> result = new Result<Object>();
//		result.setRetcode(Result.SC_ERROR);
//		result.setMsg("上传的单个文件要小于" + maxFileSize + "。多个文件大小之和要小于：" + maxRequestSize);
		logger.debug("异常了------", ex);
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}