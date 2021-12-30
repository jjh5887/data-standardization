package kvoting.intern.flowerwebapp.item;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;

public interface Item {
	ProcessType getStatus();

	void setStatus(ProcessType type);

	void setModifier(Account account);

	void setModifierName(String name);

	void setModifiedTime(LocalDateTime localDateTime);

	Long getId();

	@JsonIgnore
	String getName();

	Set<?> getRegs();
}
