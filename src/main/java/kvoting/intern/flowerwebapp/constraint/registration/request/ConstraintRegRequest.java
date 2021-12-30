package kvoting.intern.flowerwebapp.constraint.registration.request;

import io.swagger.annotations.ApiModel;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "제약사항 요청")
public class ConstraintRegRequest extends RegRequest {
	private ConstraintBase base;
}
