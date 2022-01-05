package kvoting.intern.flowerwebapp.exception.code;

import lombok.Getter;

@Getter
public enum AccountErrorCode implements ErrorCode {
	DuplicatedEmail(400, "중복된 이메일 입니다."),
	UserNotFound(400, "없는 계정입니다."),
	BadCredentials(400, "비밀번호가 일치하지 않습니다."),
	Unauthorized(403, "권한이 없습니다.");
	private int code;
	private String description;

	AccountErrorCode(int code, String description) {
		this.code = code;
		this.description = description;
	}
}
