package kvoting.intern.flowerwebapp.item.registration.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.serialize.AccountSerializer;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.RegistrationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegResponse {
	private Long id;
	private String name;
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonSerialize(using = AccountSerializer.class)
	private Account registrant;

	private RegistrationType registrationType;
	private LocalDateTime dateRegistered;

	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
	@JsonSerialize(using = AccountSerializer.class)
	private Account processor;
	private ProcessType processType;
	private LocalDateTime dateProcessed;

	private String type;
	private String errorMessage;
}
