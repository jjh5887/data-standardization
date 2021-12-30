package kvoting.intern.flowerwebapp.constraint;

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
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintReg;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.view.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "CC_CONSTRAINT_TC")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"CONSTRAINT_NAME", "CONSTRAINT_INPUT_TYPE",
	"CONSTRAINT_VALUE"}))
@JsonView(View.Public.class)
public class Constraint implements Item {
	@Id
	@GeneratedValue
	@Column(name = "CONSTRAINT_ID")
	@EqualsAndHashCode.Include
	private Long id;

	@Embedded
	private ConstraintBase base;

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
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Account modifier;

	@OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Set<ConstraintReg> regs = new HashSet<>();

	@ManyToMany(mappedBy = "constraints", fetch = FetchType.LAZY)
	@JsonView(View.Detail.class)
	@JsonIncludeProperties({"id", "base", "status"})
	private Set<CustomDomain> customDomains = new HashSet<>();

	@Override
	public String getName() {
		return base.getName();
	}
}
