package kvoting.intern.flowerwebapp.exception.code;

import lombok.Getter;

@Getter
public enum RegistrationErrorCode implements ErrorCode {
	DuplicatedRegistration(400, "중복된 아이템 입니다."),
	RegistrationNotFound(400, "등록되지 않은 요청 입니다."),
	ItemNotApproved(400, "아직 승인되지 않은 아이템 입니다."),
	NotApprovedItemExists(400, "아직 승인되지 않은 아이템이 존재합니다."),
	RegistrationAlreadyProcessed(400, "이미 처리된 요청입니다."),
	NotDeletableItem(400, "삭제할 수 없는 아이템 입니다."),
	DictNeedDomain(400, "용어에 도메인이 필요합니다.")
	;

	private int code;
	private String description;

	RegistrationErrorCode(int code, String description) {
		this.code = code;
		this.description = description;

	}
}
