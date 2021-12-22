package kvoting.intern.flowerwebapp.word.registration.request;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "단어 요청")
public class WordRegistRequest extends RegRequest {
    private WordBase base;
}
