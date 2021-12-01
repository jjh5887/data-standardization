package kvoting.intern.flowerwebapp.domain.registeration;

import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.Data;
import kvoting.intern.flowerwebapp.registration.ProcessCode;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DomainRegServiceTest {

    @Autowired
    DomainRegService domainRegService;

    @Autowired
    DomainService domainService;

    @Test
    public void registDomainAndApprove() {
        // When
        // regist
        DomainReg reg = getDomainReg();
//        reg.setName(null);
        DomainReg saveReg = domainRegService.saveDomainReg(reg);
//        assertThatThrownBy(() -> domainRegService.saveDomainReg(reg));

        // approve
        saveReg.getRegistration().setProcessCode(ProcessCode.APPROVED);
        domainRegService.saveDomainReg(saveReg);


        // Then
        Page<Domain> domainByName = domainService.getDomainByName(saveReg.getName(), PageRequest.of(0, 10));
        assertThat(domainByName.getTotalElements()).isEqualTo(1);
        List<Domain> content = domainByName.getContent();
        Domain domain = content.get(0);
        assertThat(domain.getName()).isEqualTo(saveReg.getName());
        assertThat(domain.getId()).isEqualTo(saveReg.getId());
    }

    private DomainReg getDomainReg() {
        DomainReg reg = DomainReg.builder()
                .db(DB.MYSQL)
                .data(Data.VARCHAR2)
                .registration(getRegistration())
                .nullable(true)
                .size(20)
                .name("testDomain")
                .build();
        return reg;
    }

    private Registration getRegistration() {
        return Registration.builder()
                .registrationCode(RegistrationCode.REGISTER)
                .dateRegistered(LocalDateTime.now())
                .register("kwonho")
                .processCode(ProcessCode.UNHANDLED).build();
    }

}