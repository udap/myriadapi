package io.chainmind.myriadapi.domain.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 453261861066905280L;

	protected final HttpStatus status;

	public ApiException(HttpStatus status, String message, Throwable error) {
		super(message, error);
		this.status = status;
	}

	public ApiException(HttpStatus status,String message) {
		super(message);
		this.status = status;
	}
	
	public ApiException(HttpStatus status) {
		super();
		this.status = status;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

}
