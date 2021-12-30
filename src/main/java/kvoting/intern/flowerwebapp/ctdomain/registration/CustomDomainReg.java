package kvoting.intern.flowerwebapp.ctdomain.registration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "CC_CUSTOM_DOMAIN_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "CUSTOM_DOMAIN")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "CUSTOM_DOMAIN_ID"}))
public class CustomDomainReg extends Registration<CustomDomain, CustomDomainBase> {

	@Column(name = "CUSTOM_DOMAIN_ID", insertable = false, updatable = false)
	private Long itemId;

	@Embedded
	private CustomDomainBase base;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_DOMAIN_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	private CustomDomain item;

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_CUSTOM_DOMAIN_REG_CONSTRAINT_TC",
		joinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_REG_ID"),
		inverseJoinColumns = @JoinColumn(name = "CONSTRAINT_ID"))
	private Set<Constraint> constraints = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_CUSTOM_DOMAIN_REG_WORD_TC",
		joinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_REG_ID"),
		inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private List<Word> words = new ArrayList<>();
}
