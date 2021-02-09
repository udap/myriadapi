package io.chainmind.myriadapi.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import feign.FeignException;
import io.chainmind.myriadapi.domain.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	/*@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;
	@Value("${spring.servlet.multipart.max-request-size}")
	private String maxRequestSize;*/


	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> defaultErrorHandler(Exception ex) throws Exception {
		log.error("error raised", ex);
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<String> feignExceptionHandler(FeignException feignEx) throws Exception {
		log.error("feign error: " + feignEx.getMessage(),feignEx);
		String body = feignEx.contentUTF8();
		if (!StringUtils.hasText(body)){
			body = feignEx.getMessage();
		}
		// if failed in feign executing (e.g., connection timeout) return a 503 error
		if (feignEx.status() < 0)
			return new ResponseEntity<String>(body, HttpStatus.SERVICE_UNAVAILABLE);
		// return whatever error code the upstream returns	
		return new ResponseEntity<String>(body, HttpStatus.valueOf(feignEx.status()));
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Object> apiErrorHandler(ApiException apiException) throws Exception {
		log.error("error raised", apiException);
		return new ResponseEntity<Object>(apiException.getMessage(), apiException.getStatus());
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<String> argumentErrorHandler(MethodArgumentNotValidException ex)
			throws Exception {
		log.error("error raised", ex);
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BindException.class)
	public ResponseEntity<String> bindException(HttpServletRequest req, BindException ex) throws Exception {
		log.error("error raised", ex);
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> methodArgumentTypeMismatch(HttpServletRequest req,
			MethodArgumentTypeMismatchException exception) throws Exception {
		log.error("error raised", exception);
		return new ResponseEntity<String>(exception.getMessage(),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MultipartException.class)
	public ResponseEntity<String> multipartErrorHandler(HttpServletRequest req, 
			MultipartException ex) throws Exception {
		log.error("error raised", ex);
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}

}