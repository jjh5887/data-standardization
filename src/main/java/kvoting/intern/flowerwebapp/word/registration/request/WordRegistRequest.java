package kvoting.intern.flowerwebapp.word.registration.request;

import io.swagger.annotations.ApiModelProperty;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.WordBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WordRegistRequest extends RegRequest {
    @ApiModelProperty(example = "/wordReg")
    private WordBase base;
}
