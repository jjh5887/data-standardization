package kvoting.intern.flowerwebapp.ctdomain.registration.request;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomDomainRegRequest {
    private CustomDomainBase base;
    private List<Constraint> constraints;
}
