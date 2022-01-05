package kvoting.intern.flowerwebapp.dict;

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.domain.Domain;
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

@Entity(name = "CC_DICT_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonView(View.Public.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"DICT_ENG_NAME"}))
public class Dict implements Item {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue
	@Column(name = "DICT_ID")
	private Long id;

	@Embedded
	DictBase base;

	@Column(name = "STDZ_PROC_TPCD")
	private ProcessType status;

	@Column(name = "MODFR_ID", insertable = false, updatable = false)
	private Long modifierId;

	@Column(name = "MODFR_NM")
	private String modifierName;

	@Column(name = "MODF_TM")
	private LocalDateTime modifiedTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODFR_ID", referencedColumnName = "USER_ID")
	@JsonIgnore
	private Account modifier;

	@OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<DictReg> regs = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_DICT_WORD_TC",
		joinColumns = @JoinColumn(name = "DICT_ID"),
		inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
	@JsonIgnore
	private List<Word> words = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CC_DICT_DOMAIN_TC",
		joinColumns = @JoinColumn(name = "DICT_ID"),
		inverseJoinColumns = @JoinColumn(name = "DOMAIN_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIncludeProperties({"id", "base", "status"})
	private Set<Domain> domains = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CC_DICT_CUSTOM_DOMAIN_TC",
		joinColumns = @JoinColumn(name = "DICT_ID"),
		inverseJoinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIgnoreProperties({"regs"})
	private Set<CustomDomain> customDomains = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderColumn
	@JoinTable(name = "CC_DICT_CMCD_TC",
		joinColumns = @JoinColumn(name = "DICT_ID"),
		inverseJoinColumns = @JoinColumn(name = "CMCD_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIgnoreProperties({"regs"})
	private List<CommonCode> commonCodes = new ArrayList<>();

	@PrePersist
	public void setUp() {
		switch (getBase().getCaseStyle()) {
			case SNAKE:
				makeSnakeName();
				break;
			case CAMEL:
				makeCamelName();
				break;
			default:
				throw new RuntimeException();
		}
	}

	public void makeSnakeName() {
		String name = "";
		String engName = "";
		for (Word word : words) {
			name += word.getBase().getName() + " ";
			engName += word.getBase().getEngName() + "_";
		}
		this.base.setName(name.substring(0, name.length() - 1));
		this.base.setEngName(engName.substring(0, engName.length() - 1).toUpperCase());
	}

	public void makeCamelName() {
		String name = "";
		String engName = "";
		for (int i = 0; i < words.size(); i++) {
			String nameBuf = words.get(i).getBase().getName() + " ";
			String engNameBuf = words.get(i).getBase().getEngName().toLowerCase();
			if (i > 0) {
				char c2 = Character.toUpperCase(engNameBuf.charAt(0));
				engNameBuf = c2 + engNameBuf.substring(1);
			}
			name += nameBuf;
			engName += engNameBuf;
		}
		this.base.setName(name);
		this.base.setEngName(engName);
	}

	@Override
	public String getName() {
		return base.getEngName();
	}
}