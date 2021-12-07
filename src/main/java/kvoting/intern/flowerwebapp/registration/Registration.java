package kvoting.intern.flowerwebapp.registration;


import kvoting.intern.flowerwebapp.type.ProcessType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Registration {
    @Column(name = "REGT_NM")
    private String register;
    @Column(name = "STDZ_REG_TPCD")
    private RegistrationType registrationType;
    @Column(name = "REG_TM")
    private LocalDateTime dateRegistered;

    @Column(name = "PROCR_NM")
    private String processor;
    @Column(name = "STDZ_PROCR_TPCD")
    private ProcessType processType;
    @Column(name = "PROC_TM")
    private LocalDateTime dateProcessed;

    @Column(name = "ERR_CONT", length = 500)
    private String errorMessage;

    @PrePersist
    public void made() {
        if (dateRegistered == null) {
            dateRegistered = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void processed() {
        if (dateProcessed == null) {
            dateProcessed = LocalDateTime.now();
        }
    }

}
