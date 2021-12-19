package kvoting.intern.flowerwebapp.ctdomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomDomainBase {
    @Column(name = "CUSTOM_DOMAIN_NAME")
    private String name;
    @Column(name = "CSUTOM_DOMAIN_DESC_CONT")
    private String description;
    @Column(name = "CUSTOM_DOMAIN_DB")
    private String db;
    @Column(name = "CUSTOM_DOMAIN_DATA_TYPE")
    private String dataType;
}
