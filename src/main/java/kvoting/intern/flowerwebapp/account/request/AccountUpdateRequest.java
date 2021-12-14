package kvoting.intern.flowerwebapp.account.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateRequest {
    private String name;
    private String email;
    private String password;
    private String newPassword;
    private String department;
}
