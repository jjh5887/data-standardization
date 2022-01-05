package kvoting.intern.flowerwebapp.exception.code;

import lombok.Getter;

@Getter
public enum ItemErrorCode implements ErrorCode {
	DuplicatedItem(400, "중복된 아이템 입니다."),
	ItemNotFound(400, "없는 아이템 입니다.")
	;

	private int code;
	private String description;

	ItemErrorCode(int code, String description) {
		this.code = code;
		this.description = description;
	}
}
