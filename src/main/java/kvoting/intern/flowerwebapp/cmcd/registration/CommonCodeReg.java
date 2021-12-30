package kvoting.intern.flowerwebapp.cmcd.registration;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "CC_CMCD_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "CMCD")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "CMCD_ID"}))
public class CommonCodeReg extends Registration<CommonCode, CommonCodeBase> {

	@Column(name = "CMCD_ID", insertable = false, updatable = false)
	private Long itemId;

	@Column(name = "DICT_ID", insertable = false, updatable = false)
	private Long dictId;

	@Column(name = "DICT_NM")
	private String dictName;

	@Column(name = "HIGH_DICT_ID", insertable = false, updatable = false)
	private Long highDictId;

	@Column(name = "HIGH_DICT_NM")
	private String highDictName;

	@Column(name = "HIGH_CMCD_ID", insertable = false, updatable = false)
	private Long highCmcdId;

	@Column(name = "HIGH_CMCD_NM")
	private String highCmcdName;

	@Embedded
	private CommonCodeBase base;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CMCD_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private CommonCode item;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DICT_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Dict dict;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIGH_DICT_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Dict highDict;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIGH_CMCD_ID")
	@JsonIgnore
	private CommonCode highCommonCode;
}
