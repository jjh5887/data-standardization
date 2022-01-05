package kvoting.intern.flowerwebapp.dict.registeration.request;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "용어 요청")
public class DictRegistRequest extends RegRequest {

	private DictBase base;

	@NotBlank
	@ApiModelProperty(value = "단어 id 리스트 (순서 중요)")
	private List<Long> words;

	@NotNull
	@ApiModelProperty(value = "도메인 id 리스트")
	private Set<Long> domains;

	@NotNull
	@ApiModelProperty(value = "커스텀 도메인 id 리스트")
	private Set<Long> customDomains;

	@NotNull
	@ApiModelProperty(value = "공통코드 id 리스트(순서 중요)")
	private List<Long> commonCodes;
}
