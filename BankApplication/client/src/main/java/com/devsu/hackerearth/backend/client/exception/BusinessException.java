package com.devsu.hackerearth.backend.client.exception;

public abstract class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -176644638922790649L;

	private final String code;

	protected BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

