package kvoting.intern.flowerwebapp.word;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
public class WordBase {
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z]")
	@Column(name = "WORD_ENG_NAME")
	@ApiModelProperty(value = "영어명(약어)")
	private String engName;

	@NotBlank
	@Column(name = "WORD_NAME")
	@ApiModelProperty(value = "한글명")
	private String name;

	@NotBlank
	@Column(name = "WORD_ORG_ENG_NAME")
	@ApiModelProperty(value = "원문영어명")
	private String orgEngName;
}
