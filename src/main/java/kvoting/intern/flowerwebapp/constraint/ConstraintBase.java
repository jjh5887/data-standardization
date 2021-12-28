package kvoting.intern.flowerwebapp.constraint;

import kvoting.intern.flowerwebapp.ctdomain.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConstraintBase {
    @Column(name = "CONSTRAINT_NAME")
    private String name;
    @Column(name = "CONSTRAINT_DESC_CONT")
    private String description;
    @Column(name = "CONSTRAINT_INPUT_TYPE")
    @Enumerated(EnumType.STRING)
    private InputType inputType;
    @Column(name = "CONSTRAINT_VALUE")
    private String value;
}
