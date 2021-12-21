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
public class AccountCreateRequest {
    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "비밀번호")
    private String password;
    @ApiModelProperty(value = "소속")
    private String department;
}
