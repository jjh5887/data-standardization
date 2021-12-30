package kvoting.intern.flowerwebapp.word.registration;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.view.View;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "CC_WORD_REG_TC")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "WORD")
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"REG_ID", "WORD_ID"}))
public class WordReg extends Registration<Word, WordBase> {

	@Column(name = "WORD_ID", insertable = false, updatable = false)
	private Long itemId;

	@Embedded
	@JsonView(View.Detail.class)
	WordBase base;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORD_ID")
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")

	@JsonIgnore
	private Word item;

}
