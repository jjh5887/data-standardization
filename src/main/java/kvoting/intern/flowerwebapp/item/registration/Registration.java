package kvoting.intern.flowerwebapp.item.registration;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.serialize.AccountSerializer;
import kvoting.intern.flowerwebapp.item.Item;
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
public class Registration<I extends Item, B> {

    @Id
    @GeneratedValue
    @Column(name = "REG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "REGT_ID", referencedColumnName = "USER_ID"),
            @JoinColumn(name = "REGT_NM", referencedColumnName = "USER_NM")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonSerialize(using = AccountSerializer.class)
    private Account registrant;

    @Column(name = "STDZ_REG_TPCD")
    private RegistrationType registrationType;
    @Column(name = "REG_TM")
    private LocalDateTime dateRegistered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "PROCR_ID", referencedColumnName = "USER_ID"),
            @JoinColumn(name = "PROCR_NM", referencedColumnName = "USER_NM")})
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonSerialize(using = AccountSerializer.class)
    private Account processor;
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

    public I getItem() {
        return null;
    }

    public void setItem(I item) {
    }

    public B getBase() {
        return null;
    }

    public void setBase(B base) {
    }
}


