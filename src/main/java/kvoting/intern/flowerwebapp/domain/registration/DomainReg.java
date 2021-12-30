package kvoting.intern.flowerwebapp.domain.registration;

import java.util.ArrayList;
import java.util.List;

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
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.view.View;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "CC_DOMAIN_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "DOMAIN")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "DOMAIN_ID"}))
public class DomainReg extends Registration<Domain, DomainBase> {

	@Column(name = "DOMAIN_ID", insertable = false, updatable = false)
	private Long itemId;

	@Embedded
	@JsonView(View.Detail.class)
	private DomainBase base;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAIN_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonIgnore
	private Domain item;

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderColumn
	@JoinTable(name = "CC_DOMAIN_REG_WORD_TC",
		joinColumns = @JoinColumn(name = "DOMAIN_REG_ID"),
		inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonView(View.Detail.class)
	@JsonIncludeProperties({"id", "base", "status"})
	private List<Word> words = new ArrayList<>();

	@Override
	public void registered() {
		super.registered();
	}
}