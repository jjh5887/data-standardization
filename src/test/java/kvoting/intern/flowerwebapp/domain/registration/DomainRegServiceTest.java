package kvoting.intern.flowerwebapp.domain.registration;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.domain.DB;
import kvoting.intern.flowerwebapp.domain.DataType;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.WordRegService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;

@SpringBootTest
class DomainRegServiceTest {
	@Autowired
	DomainRegService domainRegService;
	@Autowired
	DomainService domainService;
	@Autowired
	DomainRegRepository domainRegRepository;
	@Autowired
	DomainRepository domainRepository;
	@Autowired
	WordRegService wordRegService;
	@Autowired
	WordRepository wordRepository;
	@Autowired
	WordService wordService;
	@Autowired
	DictRepository dictRepository;
	@Autowired
	AccountService accountService;

	@BeforeEach
	public void setUp() {
		dictRepository.deleteAll();
		domainRepository.deleteAll();
		wordRepository.deleteAll();
	}

	@Test
	public void registCreateDomain() throws Throwable {
		// When
		// generateWords
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		List<Word> words = wordRepository.findAll();
		// create
		DomainRegistRequest domainRegistRequest = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(domainRegistRequest, account.getEmail());

		// Then
		Domain domain = (Domain)domainService.get(domainReg.getItem().getId());
		assertThat(domain.getBase().getDataType()).isEqualTo(domainReg.getBase().getDataType());
		assertThat(domain.getStatus()).isEqualTo(ProcessType.UNHANDLED);
	}

	@Test
	public void registModifyDomain() throws Throwable {
		// When
		// create
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		List<Word> words = wordRepository.findAll();
		DomainRegistRequest request = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(request, account.getEmail());
		domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account.getEmail());
		// modify
		request.getBase().setDescription("this is new domain");
		request.getBase().setNullable(false);
		DomainReg savedModifyReg = (DomainReg)domainRegService.modify(request, domainReg.getItem().getId(),
			account.getEmail());

		// Then
		DomainReg savedReg = (DomainReg)domainRegService.getRegistration(savedModifyReg.getId());
		assertThat(savedReg).isEqualTo(savedModifyReg);
		//        assertThat(savedModifyReg.getWords().size()).isEqualTo(9L);
		assertThat(domainRegRepository.count()).isEqualTo(2L);
		assertThat(wordRepository.count()).isEqualTo(10L);
	}

	@Test
	public void registDeleteDomain() throws Throwable {
		// When
		// create
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		List<Word> words = wordRepository.findAll();
		DomainRegistRequest request = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(request, account.getEmail());
		domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account.getEmail());
		// delete
		DomainReg savedDeleteReg = (DomainReg)domainRegService.delete(domainReg.getItem().getId(), account.getEmail());

		// Then
		DomainReg savedReg = (DomainReg)domainRegService.getRegistration(savedDeleteReg.getId());
		assertThat(savedReg).isEqualTo(savedDeleteReg);
		assertThat(domainRegRepository.count()).isEqualTo(2L);
		assertThat(wordRepository.count()).isEqualTo(10L);
	}

	@Test
	public void approveCreateDomain() throws Throwable {
		// When
		// generateWords
		List<Word> words = wordRepository.findAll();
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		// create
		DomainRegistRequest domainRegistRequest = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(domainRegistRequest, account.getEmail());
		// approve
		DomainReg approvedDomainReg = (DomainReg)domainRegService.process(domainReg.getId(), ProcessType.APPROVED,
			account.getEmail());

		// Then
		assertThat(domainRegRepository.count()).isEqualTo(1L);
		// ??? ????????? ??????????????? ???????????? @EqualsAndHashCode ??? ?????? id ?????? ????????? ??????
		assertThat(domainReg).isEqualTo(approvedDomainReg);
		// ????????? ????????? ??????????????? ??????
		assertThat(domainReg.getProcessType()).isNotEqualTo(approvedDomainReg.getProcessType());
		Domain domain = (Domain)domainService.get(domainReg.getItem().getId());
		assertThat(domain.getStatus()).isEqualTo(ProcessType.APPROVED);
	}

	@Test
	public void rejectCreateDomain() throws Throwable {
		// When
		// generateWords
		List<Word> words = wordRepository.findAll();
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		// create
		DomainRegistRequest domainRegistRequest = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(domainRegistRequest, account.getEmail());
		// approve
		DomainReg rejectedDomainReg = (DomainReg)domainRegService.process(domainReg.getId(), ProcessType.REJECTED,
			account.getEmail());

		// Then
		assertThat(domainRegRepository.count()).isEqualTo(1L);
		// ??? ????????? ??????????????? ???????????? @EqualsAndHashCode ??? ?????? id ?????? ????????? ??????
		assertThat(domainReg).isEqualTo(rejectedDomainReg);
		// ????????? ????????? ??????????????? ??????
		assertThat(domainReg.getProcessType()).isNotEqualTo(rejectedDomainReg.getProcessType());
		Domain domain = (Domain)domainService.get(domainReg.getItem().getId());
		assertThat(domain.getStatus()).isEqualTo(ProcessType.REJECTED);
	}

	@Test
	public void approveModifyDomain() throws Throwable {
		// When
		// create
		List<Word> words = wordRepository.findAll();
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		DomainRegistRequest request = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(request, account.getEmail());
		domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account.getEmail());
		// modify
		request.getBase().setDescription("this is new domain");
		request.getBase().setNullable(false);
		DomainReg savedModifyReg = (DomainReg)domainRegService.modify(request, domainReg.getItem().getId(),
			account.getEmail());
		// approve
		DomainReg approvedReg = (DomainReg)domainRegService.process(savedModifyReg.getId(), ProcessType.APPROVED,
			account.getEmail());

		// Then
		Domain domain = (Domain)domainService.get(approvedReg.getItem().getId());
		assertThat(domainRepository.count()).isEqualTo(1L);
		assertThat(domain.getBase().getDescription()).isEqualTo(request.getBase().getDescription());
	}

	@Test
	public void rejectModifyDomain() throws Throwable {
		// When
		// create
		List<Word> words = wordRepository.findAll();
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		DomainRegistRequest request = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(request, account.getEmail());
		domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account.getEmail());
		// modify
		request.getBase().setDescription("this is new domain");
		request.getBase().setNullable(false);
		DomainReg savedModifyReg = (DomainReg)domainRegService.modify(request, domainReg.getItem().getId(),
			account.getEmail());
		// approve
		DomainReg rejectedReg = (DomainReg)domainRegService.process(savedModifyReg.getId(), ProcessType.REJECTED,
			account.getEmail());

		// Then
		Domain domain = (Domain)domainService.get(rejectedReg.getItem().getId());
		assertThat(domainRepository.count()).isEqualTo(1L);

		Boolean nullable = domain.getBase().getNullable();
		assertThat(nullable).isEqualTo(true);
	}

	@Test
	public void approveDeleteDomain() throws Throwable {
		// When
		// create
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);
		List<Word> words = wordRepository.findAll();
		DomainRegistRequest request = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(request, account.getEmail());
		domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account.getEmail());

		// delete
		DomainReg savedModifyReg = (DomainReg)domainRegService.delete(domainReg.getItem().getId(), account.getEmail());
		// approve
		DomainReg processReg = (DomainReg)domainRegService.process(savedModifyReg.getId(), ProcessType.APPROVED,
			account.getEmail());

		// Then
		assertThat(domainRegRepository.count()).isEqualTo(2L);
		assertThat(domainRepository.count()).isEqualTo(1L);
		assertThat(processReg.getItem().getStatus()).isEqualTo(ProcessType.DELETABLE);
	}

	@Test
	public void rejectDeleteDomain() throws Throwable {
		// When
		// create
		List<Word> words = wordRepository.findAll();
		Account account = accountService.getAccount("test@test.com");
		generateWords(account);

		DomainRegistRequest request = makeDomRequest(words);
		DomainReg domainReg = (DomainReg)domainRegService.create(request, account.getEmail());
		domainRegService.process(domainReg.getId(), ProcessType.APPROVED, account.getEmail());
		// delete
		DomainReg savedModifyReg = (DomainReg)domainRegService.delete(domainReg.getItem().getId(), account.getEmail());
		// reject
		domainRegService.process(savedModifyReg.getId(), ProcessType.REJECTED, account.getEmail());

		// Then
		assertThat(domainRegRepository.count()).isEqualTo(2L);
		assertThat(domainRepository.count()).isEqualTo(1L);
	}

	private DomainRegistRequest makeDomRequest(List<Word> words) {
		return DomainRegistRequest.builder()
			.base(makeDomainBase())
			.build();
	}

	private DomainBase makeDomainBase() {
		return DomainBase.builder()
			.size(20)
			.scale(0)
			.nullable(true)
			.description("this is test domain")
			.db(DB.ORACLE)
			.dataType(DataType.VARCHAR2)
			.build();
	}

	private WordRegistRequest makeRequest(int idx) {
		return WordRegistRequest.builder()
			.base(WordBase.builder()
				.engName("TST" + idx)
				.name("?????????" + idx)
				.orgEngName("TEST" + idx)
				.build()
			)
			.build();
	}

	public void generateWords(Account account) throws Throwable {
		List<WordRegistRequest> requests = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			requests.add(makeRequest(i));
		}

		for (WordRegistRequest request : requests) {
			wordRegService.create(request, account.getEmail());
		}
	}
}