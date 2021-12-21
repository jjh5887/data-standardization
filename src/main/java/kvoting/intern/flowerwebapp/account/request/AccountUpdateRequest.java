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
public class AccountUpdateRequest {
    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "기존 비밀번호")
    private String password;
    @ApiModelProperty(value = "새 비밀번호")
    private String newPassword;
    @ApiModelProperty(value = "소속")
    private String department;
}
