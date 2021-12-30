package kvoting.intern.flowerwebapp.dict.registeration.request;

import java.util.List;
import java.util.Set;

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
	@ApiModelProperty(value = "단어 id 리스트 (순서 중요)")
	private List<Long> words;
	@ApiModelProperty(value = "도메인 id 리스트")
	private Set<Long> domains;
	@ApiModelProperty(value = "커스텀 도메인 id 리스트")
	private Set<Long> customDomains;
	@ApiModelProperty(value = "공통 코드 id")
	private Long commonCode;
}
