package kvoting.intern.flowerwebapp.account.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDeleteRequest {
	@ApiModelProperty(value = "비밀번호")
	private String password;
}
