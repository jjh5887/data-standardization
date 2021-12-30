package kvoting.intern.flowerwebapp.ctdomain.registration.request;

import java.util.List;
import java.util.Set;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "커스텀 도메인 요청")
public class CustomDomainRegRequest extends RegRequest {
	private CustomDomainBase base;

	@ApiModelProperty(value = "제약사항 id 리스트")
	private Set<Long> constraints;

	@ApiModelProperty(value = "단어 id 리스트(순서 중요)")
	private List<Long> words;
}
