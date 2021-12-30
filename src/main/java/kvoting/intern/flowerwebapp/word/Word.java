package kvoting.intern.flowerwebapp.word;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.registration.CustomDomainReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.view.View;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "CC_WORD_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonView(View.Public.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"WORD_ENG_NAME", "WORD_NAME", "WORD_ORG_ENG_NAME"}))
public class Word implements Item {
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue
	@Column(name = "WORD_ID")
	private Long id;

	@Embedded
	private WordBase base;

	@Column(name = "WORD_PROC_TPCD")
	private ProcessType status;

	@Column(name = "MODFR_ID", insertable = false, updatable = false)
	private Long modifierId;

	@Column(name = "MODFR_NM")
	private String modifierName;

	@Column(name = "MODF_TM")
	private LocalDateTime modifiedTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODFR_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Account modifier;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.REMOVE)
	@JsonView(View.Detail.class)
	private Set<WordReg> regs = new HashSet<>();

	@ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<DictReg> dictRegs = new HashSet<>();

	@ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Set<Dict> dicts = new HashSet<>();

	@ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Set<Domain> domains = new HashSet<>();

	@ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<DomainReg> domainRegs = new HashSet<>();

	@ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Set<CustomDomain> customDomains = new HashSet<>();

	@ManyToMany(mappedBy = "words", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<CustomDomainReg> customDomainRegs = new HashSet<>();

	@Override
	public String getName() {
		return base.getEngName();
	}
}
