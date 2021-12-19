package kvoting.intern.flowerwebapp.constraint.registration.request;

import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConstraintRegRequest {
    private ConstraintBase base;
}
