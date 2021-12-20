package kvoting.intern.flowerwebapp.domain.registration.request;

import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class DomainRegistRequest extends RegRequest {
    private DomainBase base;
}
