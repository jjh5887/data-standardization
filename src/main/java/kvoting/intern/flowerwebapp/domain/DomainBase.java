package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@lombok.Data
public class DomainBase {
    @Column(name = "DOMAIN_NAME")
    private String name;
    @Column(name = "DOMAIN_DESC_CONT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "DBTYPE_TPCD")
    private DB db;
    @Enumerated(EnumType.STRING)
    @Column(name = "DATATYPE_TPCD")
    private DataType dataType;
    @Column(name = "DOMAIN_SIZE")
    private Integer size;
    @Column(name = "DOMAIN_SCALE")
    private Integer scale;
    @Column(name = "NN_YN")
    private Boolean nullable;
}
