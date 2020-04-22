package io.chainmind.myriadapi.domain.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	protected Object body;

	public ApiException(HttpStatus status,Object body, Throwable error) {
		super(error.getMessage(), error);
		this.status = status;
		this.body = body;
	}

	public ApiException(HttpStatus status,Object body) {
		this.status = status;
		this.body = body;
	}
	
	public ApiException(HttpStatus status) {
		this.status = status;
		this.body = status.getReasonPhrase();
	}
	
//	public ApiException(String body) {
//		this.status = status;
//		this.body = body;
//	}
//	public ApiException() {
//		this.status = status;
//		this.body = status.getReasonPhrase();
//	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
