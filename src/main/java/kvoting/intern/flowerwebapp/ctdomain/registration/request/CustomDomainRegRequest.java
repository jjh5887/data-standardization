package kvoting.intern.flowerwebapp.ctdomain.registration.request;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class CustomDomainRegRequest extends RegRequest {
    private CustomDomainBase base;
    private List<Constraint> constraints;
}
