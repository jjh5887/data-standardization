package kvoting.intern.flowerwebapp.domain.registration.request;

import kvoting.intern.flowerwebapp.domain.DomainBase;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DomainRegistRequest {
    private DomainBase base;
}
