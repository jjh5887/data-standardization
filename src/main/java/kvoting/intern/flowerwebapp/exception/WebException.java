package kvoting.intern.flowerwebapp.exception;

import kvoting.intern.flowerwebapp.exception.code.ErrorCode;
import lombok.Data;

@Data
public class WebException extends RuntimeException {
	private int status;

	public WebException(ErrorCode errorCode) {
		super(errorCode.getDescription());
		this.status = errorCode.getCode();
	}
}
