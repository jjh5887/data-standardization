package kvoting.intern.flowerwebapp.domain.registration.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "도메인 요청")
public class DomainRegistRequest extends RegRequest {
	private DomainBase base;
	@NotBlank
	@ApiModelProperty(value = "단어 id 리스트 (순서 중요)")
	private List<Long> words;
}
