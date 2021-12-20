package kvoting.intern.flowerwebapp.constraint.registration.request;

import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ConstraintRegRequest extends RegRequest {
    private ConstraintBase base;
}
