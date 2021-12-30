package kvoting.intern.flowerwebapp.constraint;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConstraintBase {
	@ApiModelProperty(value = "이름")
	@Column(name = "CONSTRAINT_NAME")
	private String name;

	@ApiModelProperty(value = "설명")
	@Column(name = "CONSTRAINT_DESC_CONT")
	private String description;

	@ApiModelProperty(value = "입력 타입")
	@Column(name = "CONSTRAINT_INPUT_TYPE")
	@Enumerated(EnumType.STRING)
	private InputType inputType;

	@ApiModelProperty(value = "값")
	@Column(name = "CONSTRAINT_VALUE")
	private String value;
}
