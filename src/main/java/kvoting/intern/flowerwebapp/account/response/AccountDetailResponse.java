package kvoting.intern.flowerwebapp.account.response;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import kvoting.intern.flowerwebapp.account.AccountRole;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailResponse {
    private Long id;
    private String email;
    private String name;
    private String department;
    private Set<AccountRole> roles;

    @JsonIncludeProperties({
            "id", "name",
            "registrant", "registrationType", "dateRegistered",
            "processor", "processType", "dateProcessed"
    })
    private Set<Registration> regs;
    @JsonIncludeProperties({
            "id", "name",
            "registrant", "registrationType", "dateRegistered",
            "processor", "processType", "dateProcessed"
    })
    private Set<Registration> processRegs;
}