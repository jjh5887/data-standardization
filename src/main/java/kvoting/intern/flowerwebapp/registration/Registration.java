package kvoting.intern.flowerwebapp.registration;


import kvoting.intern.flowerwebapp.account.Account;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity(name = "CC_REG_TC")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "REG_TYPE")
@EqualsAndHashCode(of = "id")
public class Registration {

    @Id
    @GeneratedValue
    @Column(name = "REG_ID")
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "REGT_ID", referencedColumnName = "USER_ID"),
            @JoinColumn(name = "REGT_NM", referencedColumnName = "USER_NM")})
    private Account registrant;
    @Column(name = "STDZ_REG_TPCD")
    private RegistrationType registrationType;
    @Column(name = "REG_TM")
    private LocalDateTime dateRegistered;

    @Column(name = "STDZ_PROCR_TPCD")
    private ProcessType processType;
    @Column(name = "PROC_TM")
    private LocalDateTime dateProcessed;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PROCR_ID", referencedColumnName = "USER_ID"),
            @JoinColumn(name = "PROCR_NM", referencedColumnName = "USER_NM")})
    private Account processor;

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


