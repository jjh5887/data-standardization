package kvoting.intern.flowerwebapp.ctdomain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.constraint.Constraint;
import kvoting.intern.flowerwebapp.ctdomain.registration.CustomDomainReg;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.view.View;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "CC_CUSTOM_DOMAIN_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"CUSTOM_DOMAIN_NAME", "CUSTOM_DOMAIN_DB",
	"CUSTOM_DOMAIN_DATA_TYPE"}))
@JsonView(View.Public.class)
public class CustomDomain implements Item {
	@Id
	@GeneratedValue
	@Column(name = "CUSTOM_DOMAIN_ID")
	@EqualsAndHashCode.Include
	private Long id;

	@Embedded
	private CustomDomainBase base;

	@Column(name = "STDZ_PROC_TPCD")
	private ProcessType status;

	@Column(name = "MODFR_NM")
	private String modifierName;

	@Column(name = "MODF_TM")
	private LocalDateTime modifiedTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODFR_ID", referencedColumnName = "USER_ID")
	@JsonIgnore
	private Account modifier;

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_CUSTOM_DOMAIN_CONSTRAINT_TC",
		joinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_ID"),
		inverseJoinColumns = @JoinColumn(name = "CONSTRAINT_ID"))
	@JsonView(View.Detail.class)
	private Set<Constraint> constraints = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_CUSTOM_DOMAIN_WORD_TC",
		joinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_ID"),
		inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
	@JsonIgnore
	private List<Word> words = new ArrayList<>();

	@OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<CustomDomainReg> regs = new HashSet<>();

	@ManyToMany(mappedBy = "customDomains", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<DictReg> dictRegs = new HashSet<>();

	@ManyToMany(mappedBy = "customDomains", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Dict> dicts = new HashSet<>();

	@Override
	public String getName() {
		return base.getName();
	}

	@PrePersist
	public void setUp() {
		StringBuilder name = new StringBuilder();
		for (Word word : words) {
			name.append(word.getBase().getName()).append(" ");
		}
		base.setName(name.substring(0, name.length() - 1));
	}
}
