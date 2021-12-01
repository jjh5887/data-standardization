package kvoting.intern.flowerwebapp.registration;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Registration {

    @Column(name = "STDZ_REG_TPCD", nullable = false)
    private RegistrationCode registrationCode;
    @Column(name = "STDZ_PROC_TPCD", nullable = false)
    private ProcessCode processCode;

    @Column(name = "REG_TM", nullable = false)
    private LocalDateTime dateRegistered;
    @Column(name = "REGT_NM", nullable = false)
    private String register;

    @Column(name = "PROC_TM")
    private LocalDateTime dateProcessed;
    @Column(name = "PROCR_NM")
    private String processor;

    @Column(name = "ERR_CONT", length = 500)
    private String errorMessage;
}
