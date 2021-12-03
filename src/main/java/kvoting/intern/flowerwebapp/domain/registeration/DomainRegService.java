package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.type.ProcessType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainRegService {
    private final DomainRegRepository domainRegRepository;
    private final DomainService domainService;
    private final ModelMapper modelMapper;

    public DomainReg save(DomainReg reg) {
        if (reg.getProcessType() == ProcessType.APPROVED) {
            Domain map = modelMapper.map(reg, Domain.class);
            map.setId(reg.getId());
            domainService.save(map);
        }
        return domainRegRepository.save(reg);
    }

    public Page<DomainReg> getByName(String name, Pageable pageable) {
        return domainRegRepository.findByNameContains(name, pageable);
    }
}
