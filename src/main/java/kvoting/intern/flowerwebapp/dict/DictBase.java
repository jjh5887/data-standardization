package kvoting.intern.flowerwebapp.dict;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

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
public class DictBase {
	@Null
	@Column(name = "DICT_NAME")
	@ApiModelProperty(hidden = true)
	private String name;

	@Null
	@Column(name = "DICT_ENG_NAME")
	@ApiModelProperty(hidden = true)
	private String engName;

	@NotBlank
	@ApiModelProperty(value = "표기 방식")
	@Column(name = "DICT_CASE")
	private CaseStyle caseStyle;

	@NotBlank
	@Column(name = "DICT_SCR_NAME")
	@ApiModelProperty(value = "화면에 표시되는 이름")
	private String screenName;

	@ApiModelProperty(value = "설명")
	@Column(name = "DICT_DESC_CONT")
	private String description;

	@Null
	@ApiModelProperty(hidden = true)
	@Column(name = "DICT_CMCD_YN")
	private Boolean isCommon;
}
