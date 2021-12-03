package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.Data;
import kvoting.intern.flowerwebapp.type.ProcessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DomainBase {
    @Column(name = "DOMAIN_NAME", nullable = false)
    private String name;
    @Column(name = "DOMAIN_ENG_NAME", nullable = false)
    private String engName;
    @Column(name = "DOMAIN_DESC_CONT", length = 200)
    private String description;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "DBTYPE_TPCD", nullable = false, length = 2)
    private DB db;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "DATATYPE_TPCD", nullable = false, length = 2)
    private Data data;
    @Column(name = "DOMAIN_SIZE", nullable = false, length = 4)
    private int size;
    @Column(name = "DOMAIN_SCALE", length = 2)
    private int scale;
    @Column(name = "NN_YN")
    private boolean nullable;
    @Column(name = "MODF_TM")
    private LocalDateTime modifiedTime;
    @Column(name = "MODF_NM")
    private String modifier;
    @Column(name = "STDZ_PROC_TPCD", nullable = false)
    private ProcessType processType;

    @PrePersist
    public void setUp() {
        if (processType == null) {
            processType = ProcessType.UNHANDLED;
        }
    }
}
