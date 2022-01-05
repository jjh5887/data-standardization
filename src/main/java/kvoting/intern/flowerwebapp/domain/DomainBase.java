package kvoting.intern.flowerwebapp.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@lombok.Data
public class DomainBase {
	@Null
	@Column(name = "DOMAIN_NAME")
	@ApiModelProperty(hidden = true)
	private String name;

	@Column(name = "DOMAIN_DESC_CONT")
	@ApiModelProperty(value = "설명")
	private String description;

	@NotBlank
	@Enumerated(EnumType.STRING)
	@Column(name = "DB_TPCD")
	@ApiModelProperty(value = "디비 종류")
	private DB db;

	@NotBlank
	@Enumerated(EnumType.STRING)
	@Column(name = "DATA_TPCD")
	@ApiModelProperty(value = "데이터 타입")
	private DataType dataType;

	@Column(name = "DOMAIN_SIZE")
	@ApiModelProperty(value = "길이")
	private Integer size;

	@Column(name = "DOMAIN_SCALE")
	@ApiModelProperty(value = "영어명(약어)")
	private Integer scale;

	@Column(name = "NN_YN")
	@ApiModelProperty(value = "null 가능 여부", dataType = "boolean")
	private Boolean nullable;
}
