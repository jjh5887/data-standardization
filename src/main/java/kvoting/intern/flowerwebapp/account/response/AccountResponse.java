package kvoting.intern.flowerwebapp.account.response;

import kvoting.intern.flowerwebapp.account.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String email;
    private String name;
    private String department;
    private Set<AccountRole> roles;
}
