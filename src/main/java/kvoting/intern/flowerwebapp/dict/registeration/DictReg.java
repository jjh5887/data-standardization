package kvoting.intern.flowerwebapp.dict.registeration;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.view.View;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "CC_DICT_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "DICT")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "DICT_ID"}))
public class DictReg extends Registration<Dict, DictBase> {

	@Column(name = "DICT_ID", insertable = false, updatable = false)
	private Long itemId;

	@Embedded
	@JsonView(View.Detail.class)
	private DictBase base;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DICT_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Dict item;

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_DICT_REG_WORD_TC",
		joinColumns = @JoinColumn(name = "DICT_REG_ID"),
		inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIncludeProperties({"id", "base", "status"})
	private List<Word> words = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CC_DICT_REG_DOMAIN_TC",
		joinColumns = @JoinColumn(name = "DICT_REG_ID"),
		inverseJoinColumns = @JoinColumn(name = "DOMAIN_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIncludeProperties({"id", "base", "status"})
	private Set<Domain> domains = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CC_DICT_REG_CUSTOM_DOMAIN_TC",
		joinColumns = @JoinColumn(name = "DICT_REG_ID"),
		inverseJoinColumns = @JoinColumn(name = "CUSTOM_DOMAIN_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIgnoreProperties({"regs"})
	private Set<CustomDomain> customDomains = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIgnoreProperties({"regs"})
	private CommonCode commonCode;
}
