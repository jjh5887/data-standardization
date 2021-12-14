package kvoting.intern.flowerwebapp.registration;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class Registration {
    @Column(name = "STDZ_REG_TPCD")
    private RegistrationType registrationType;
    @Column(name = "REG_TM")
    private LocalDateTime dateRegistered;

    @Column(name = "STDZ_PROCR_TPCD")
    private ProcessType processType;
    @Column(name = "PROC_TM")
    private LocalDateTime dateProcessed;

    @Column(name = "ERR_CONT", length = 500)
    private String errorMessage;

    @PrePersist
    public void registered() {
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


