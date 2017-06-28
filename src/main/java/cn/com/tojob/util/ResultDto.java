package cn.com.tojob.util;

import java.io.Serializable;

public class ResultDto implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SUCCESS = "success";
	public static final String WARNING = "warning";
	public static final String ERROR = "error";

	private String type;
	private int code;
	private String message;
	private Object data;

	public ResultDto() {
		setType(SUCCESS);
	}

	public void resultError() {
		this.setType(ERROR);
	}

	public void resultWaring() {
		this.setType(WARNING);
	}

	public void resultSuccess() {
		this.setType(SUCCESS);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
