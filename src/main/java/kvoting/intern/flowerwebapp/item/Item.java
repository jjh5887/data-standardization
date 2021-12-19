package kvoting.intern.flowerwebapp.item;

import kvoting.intern.flowerwebapp.item.registration.ProcessType;

public interface Item {
    ProcessType getStatus();

    void setStatus(ProcessType type);
}
