package kvoting.intern.flowerwebapp.item;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;

import java.time.LocalDateTime;

public interface Item {
    ProcessType getStatus();

    void setStatus(ProcessType type);

    void setModifier(Account account);

    void setModifierName(String name);

    void setModifiedTime(LocalDateTime localDateTime);

    String getName();
}
