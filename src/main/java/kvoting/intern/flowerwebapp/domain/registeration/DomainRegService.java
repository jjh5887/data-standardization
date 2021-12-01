package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.registration.ProcessCode;
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

    public DomainReg saveDomainReg(DomainReg reg) {
        if (reg.getRegistration().getProcessCode() == ProcessCode.APPROVED) {
            Domain map = modelMapper.map(reg, Domain.class);
            map.setId(reg.getId());
            domainService.saveDomain(map);
        }
        return domainRegRepository.save(reg);
    }

    public Page<DomainReg> getDomainRegByName(String name, Pageable pageable) {
        return domainRegRepository.findByNameContains(name, pageable);
    }
}
