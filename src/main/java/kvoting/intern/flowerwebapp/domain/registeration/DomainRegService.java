package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registeration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationType;
import kvoting.intern.flowerwebapp.type.ProcessType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class DomainRegService {
    private final DomainRegRepository domainRegRepository;
    private final DomainService domainService;
    private final ModelMapper modelMapper;

    public DomainReg getDomainReg(Long id) {
        return domainRegRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public DomainReg create(DomainRegistRequest request) {
        // create mappedDomain
        Domain mappedDomain = modelMapper.map(request, Domain.class);
        mappedDomain.setStatus(ProcessType.UNHANDLED);
        mappedDomain.setDomainRegs(new HashSet<>());
        Domain domain = domainService.save(mappedDomain);

        // create domain create_reg
        return domainRegRepository
                .save(generateDomainReg(request, domain, RegistrationType.CREATE));
    }

    public DomainReg modify(DomainRegistRequest request, Long id) {
        // find domain
        Domain domain = domainService.getDomain(id);

        // create domain modify_reg
        return domainRegRepository.save(generateDomainReg(request, domain, RegistrationType.MODIFY));
    }

    public DomainReg delete(Long id) {
        // find domain
        Domain domain = domainService.getDomain(id);

        // create domain delete_reg
        return domainRegRepository.save(generateDomainReg(domain));
    }

    public DomainReg processDomainReg(Long id, ProcessType type) {
        DomainReg domainReg = getDomainReg(id);
        domainReg.getRegistration().setProcessType(type);
        if (type == ProcessType.REJECTED) {
            if (domainReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                domainReg.getDomain().setStatus(type);
                domainService.save(domainReg.getDomain());
            }
        }
        if (type == ProcessType.APPROVED) {
            if (domainReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                domainReg.getDomain().setStatus(type);
                domainService.save(domainReg.getDomain());
            }
            if (domainReg.getRegistration().getRegistrationType() == RegistrationType.MODIFY) {
                Domain domain = domainService.getDomain(domainReg.getDomain().getId());
                modelMapper.map(domainReg.getDomainBase(), domain);
                domain.getWords().clear();
                domain.getWords().addAll(domainReg.getWords());
                domainService.save(domain);
                domainReg.setDomain(domain);
            }
            if (domainReg.getRegistration().getRegistrationType() == RegistrationType.DELETE) {
                domainService.delete(domainReg.getDomain());
                return null;
            }
        }
        return domainRegRepository.save(domainReg);
    }

    private DomainReg generateDomainReg(Domain domain) {
        return DomainReg.builder()
                .domain(domain)
                .registration(generateReg(RegistrationType.DELETE))
                .build();
    }

    private DomainReg generateDomainReg(DomainRegistRequest request, Domain domain, RegistrationType type) {
        DomainReg map = modelMapper.map(request, DomainReg.class);
        map.setDomain(domain);
        map.setRegistration(generateReg(type));
        return map;
    }

    private Registration generateReg(RegistrationType type) {
        return Registration.builder()
                .register("admin")
                .registrationType(type)
                .processType(ProcessType.UNHANDLED)
                .build();
    }
}
