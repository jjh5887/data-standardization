package kvoting.intern.flowerwebapp.domain.registration.request;

import io.swagger.annotations.ApiModel;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "도메인 요청")
public class DomainRegistRequest extends RegRequest {
    private DomainBase base;
}
