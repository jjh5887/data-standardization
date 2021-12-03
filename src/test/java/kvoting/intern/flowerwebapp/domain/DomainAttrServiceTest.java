package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DomainAttrServiceTest {

    @Autowired
    DomainService domainService;

    @Autowired
    DomainRepository domainRepository;

    @Test
    public void saveDomain() {
        Domain domain = getDomain();
        Domain saveDomain = domainService.save(domain);

        assertThat(saveDomain.getName()).isEqualTo(domain.getName());
        assertThat(saveDomain.getDescription()).isEqualTo(domain.getDescription());
        assertThat(saveDomain.getDb()).isEqualTo(domain.getDb());
        assertThat(saveDomain.getModifiedTime()).isEqualTo(domain.getModifiedTime());
        assertThat(saveDomain.getModifier()).isNull();
    }

    @Test
    public void saveWrongDomain() {
        Domain domain = getDomain();
        domain.setDb(null);

        assertThatThrownBy(() -> {
            domainService.save(domain);
        });
    }

    @Test
    public void findByName() {
        Domain saveDomain = domainService.save(getDomain());
        Page<Domain> domainByName = domainService.getByName(saveDomain.getName(), PageRequest.of(0, 1));
        for (Domain domain : domainByName) {
            assertThat(domain.getName()).isEqualTo(saveDomain.getName());
        }
    }

    private Domain getDomain() {
        Domain domain = Domain.builder()
                .db(DB.MYSQL)
                .data(Data.CHAR)
                .description("it is test Domain")
                .name("test")
                .modifiedTime(LocalDateTime.now())
                .nullable(true)
                .scale(2)
                .size(10)
                .build();
        return domain;
    }


    private List<Domain> getDomains() {
        List<Domain> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(Domain.builder()
                    .db(DB.MYSQL)
                    .data(Data.CHAR)
                    .description("it is test Domain")
                    .name("test " + i)
                    .modifiedTime(LocalDateTime.now())
                    .nullable(true)
                    .scale(2)
                    .size(10)
                    .build());
        }
        return list;
    }
}