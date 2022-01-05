package kvoting.intern.flowerwebapp.item.registration;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.serialize.AccountSerializer;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.view.View;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "REG_ITEM_ID"}))
public class Registration<I extends Item, B> {

	@Id
	@GeneratedValue
	@Column(name = "REG_ID")
	private Long id;

	@Column(name = "REG_ITEM_NM")
	private String itemName;

	@Column(name = "REG_ITEM_ID")
	private Long itemId;

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

	public B getBase() {
		return null;
	}

	public void setBase(B base) {
	}
}


