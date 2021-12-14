package kvoting.intern.flowerwebapp.account.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDeleteRequest {
    private String password;
}
