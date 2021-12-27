package kvoting.intern.flowerwebapp.item.registration;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.serialize.AccountSerializer;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.view.View;
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
@JsonView(View.Public.class)
public class Registration<I extends Item, B> {

    @Id
    @GeneratedValue
    @Column(name = "REG_ID")
    private Long id;

    @Column(name = "REG_ITEM_NM")
    private String itemName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGT_ID", referencedColumnName = "USER_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonSerialize(using = AccountSerializer.class)
    private Account registrant;

    @Column(name = "STDZ_REG_TPCD")
    private RegistrationType registrationType;
    @Column(name = "REG_TM")
    private LocalDateTime dateRegistered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROCR_ID", referencedColumnName = "USER_ID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
    @JsonSerialize(using = AccountSerializer.class)
    private Account processor;

    @Column(name = "STDZ_PROCR_TPCD")
    private ProcessType processType;
    @Column(name = "PROC_TM")
    private LocalDateTime dateProcessed;

    @Column(name = "REG_TYPE", insertable = false, updatable = false)
    private String type;

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

    public Long getItemId() {
        return null;
    }

    public void setItemId(Long id) {
    }

    public B getBase() {
        return null;
    }

    public void setBase(B base) {
    }
}


