package kvoting.intern.flowerwebapp.ctdomain;

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
public class CustomDomainBase {
	@Null
	@ApiModelProperty(hidden = true)
	@Column(name = "CUSTOM_DOMAIN_NAME")
	private String name;

	@ApiModelProperty(value = "설명")
	@Column(name = "CSUTOM_DOMAIN_DESC_CONT")
	private String description;

	@NotBlank
	@ApiModelProperty(value = "DB 이름")
	@Column(name = "CUSTOM_DOMAIN_DB")
	private String db;

	@NotBlank
	@ApiModelProperty(value = "데이터 타입 이름")
	@Column(name = "CUSTOM_DOMAIN_DATA_TYPE")
	private String dataType;
}
