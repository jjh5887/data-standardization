package kvoting.intern.flowerwebapp.cmcd.registration;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRepository;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeRepository;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.constraint.ConstraintRepository;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainRepository;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictRepository;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;

@SpringBootTest
class CommonCodeRegServiceTest {

	@Autowired
	CommonCodeRegService commonCodeRegService;

	@Autowired
	CommonCodeService commonCodeService;

	@Autowired
	DictService dictService;

	@Autowired
	DictRegService dictRegService;

	@Autowired
	WordService wordService;

	@Autowired
	CommonCodeRepository commonCodeRepository;

	@Autowired
	CommonCodeRegRepository commonCodeRegRepository;

	@Autowired
	ApplicationRunner applicationRunner;

	@Autowired
	DictRepository dictRepository;

	@Autowired
	DomainRepository domainRepository;

	@Autowired
	WordRepository wordRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ConstraintRepository constraintRepository;

	@Autowired
	CustomDomainRepository customDomainRepository;

	@BeforeEach
	public void setUp() throws Exception {
		commonCodeRepository.deleteAll();
		dictRepository.deleteAll();
		domainRepository.deleteAll();
		wordRepository.deleteAll();
		customDomainRepository.deleteAll();
		constraintRepository.deleteAll();
		accountRepository.deleteAll();
		applicationRunner.run(null);
		commonCodeRepository.deleteAll();
	}

	@Test
	public void registCreateCmcd() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("addr");
		Account account = accountService.getAccount("test@test.com");

		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// Then
		assertThat(commonCodeReg.getDict()).isEqualTo(dict);
		dict = (Dict)dictService.get(dict.getId());
		CommonCode commonCode = (CommonCode)commonCodeService.get(commonCodeReg.getItem().getId());

		assertThat(dict.getCommonCode()).isEqualTo(commonCode);
		assertThat(dict.getCommonCode().getBase().getCodeName()).isEqualTo(commonCode.getBase().getCodeName());
		assertThat(dict.getCommonCode().getBase().getCode()).isEqualTo(commonCode.getBase().getCode());
		assertThat(dict.getCommonCode().getBase().getDescription()).isEqualTo(commonCode.getBase().getDescription());
		assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
	}

	@Test
	public void registModifyCmcd() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("phn");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// modify
		cmcdRegRequest.getBase().setCode("B01");
		Word clb = wordService.getByEng("addr");
		CommonCodeReg modify = (CommonCodeReg)commonCodeRegService.modify(cmcdRegRequest,
			commonCodeReg.getItem().getId(), account);

		// Then
		CommonCodeReg modifyReg = (CommonCodeReg)commonCodeRegService.getRegistration(modify.getId());

		assertThat(modifyReg.getBase().getCodeName()).isEqualTo(clb.getBase().getName());
		assertThat(commonCodeReg.getDict()).isEqualTo(dict);

		dict = (Dict)dictService.get(dict.getId());
		CommonCode commonCode = (CommonCode)commonCodeService.get(commonCodeReg.getItem().getId());
		assertThat(dict.getCommonCode()).isEqualTo(commonCode);
		assertThat(dict.getCommonCode().getBase().getCodeName()).isEqualTo(commonCode.getBase().getCodeName());
		assertThat(dict.getCommonCode().getBase().getCode()).isEqualTo(commonCode.getBase().getCode());
		assertThat(dict.getCommonCode().getBase().getDescription()).isEqualTo(commonCode.getBase().getDescription());
		assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
	}

	@Test
	public void registDeleteCommonCode() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("addr");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// delete
		commonCodeRegService.delete(commonCodeReg.getItem().getId(), account);

		// Then
		assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
		assertThat(commonCodeRepository.count()).isEqualTo(1L);

		assertThat(commonCodeReg.getDict()).isEqualTo(dict);
		dict = (Dict)dictService.get(dict.getId());
		CommonCode commonCode = (CommonCode)commonCodeService.get(commonCodeReg.getItem().getId());

		assertThat(dict.getCommonCode()).isEqualTo(commonCode);
		assertThat(dict.getCommonCode().getBase().getCodeName()).isEqualTo(commonCode.getBase().getCodeName());
		assertThat(dict.getCommonCode().getBase().getCode()).isEqualTo(commonCode.getBase().getCode());
		assertThat(dict.getCommonCode().getBase().getDescription()).isEqualTo(commonCode.getBase().getDescription());
		assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
	}

	@Test
	public void approveCreateCmcd() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("phn");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// approve
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// Then
		assertThat(commonCodeReg.getDict()).isEqualTo(dict);
		dict = (Dict)dictService.get(dict.getId());
		CommonCode commonCode = (CommonCode)commonCodeService.get(commonCodeReg.getItem().getId());

		assertThat(commonCodeRepository.count()).isEqualTo(1L);
		assertThat(commonCodeRegRepository.count()).isEqualTo(1L);

		assertThat(dict.getCommonCode()).isEqualTo(commonCode);
		assertThat(dict.getCommonCode().getBase().getCodeName()).isEqualTo(commonCode.getBase().getCodeName());
		assertThat(dict.getCommonCode().getBase().getCode()).isEqualTo(commonCode.getBase().getCode());
		assertThat(dict.getCommonCode().getBase().getDescription()).isEqualTo(commonCode.getBase().getDescription());
		assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
		assertThat(commonCode.getStatus()).isEqualTo(ProcessType.APPROVED);
	}

	@Test
	public void rejectCreateCmcd() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("phn");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// reject
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.REJECTED, account);

		// Then
		assertThat(commonCodeReg.getDict()).isEqualTo(dict);
		dict = (Dict)dictService.get(dict.getId());
		CommonCode commonCode = (CommonCode)commonCodeService.get(commonCodeReg.getItem().getId());

		assertThat(commonCodeRepository.count()).isEqualTo(1L);
		assertThat(commonCodeRegRepository.count()).isEqualTo(1L);

		assertThat(dict.getCommonCode()).isEqualTo(commonCode);
		assertThat(dict.getCommonCode().getBase().getCodeName()).isEqualTo(commonCode.getBase().getCodeName());
		assertThat(dict.getCommonCode().getBase().getCode()).isEqualTo(commonCode.getBase().getCode());
		assertThat(dict.getCommonCode().getBase().getDescription()).isEqualTo(commonCode.getBase().getDescription());
		assertThat(dict.getCommonCode().getStatus()).isEqualTo(commonCode.getStatus());
		assertThat(commonCode.getStatus()).isEqualTo(ProcessType.REJECTED);
	}

	@Test
	public void approveModifyCmcd() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("phn");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		Account account = accountService.getAccount("test@test.com");
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// modify
		cmcdRegRequest.getBase().setCode("B01");
		Word clb = wordService.getByEng("addr");
		CommonCodeReg modify = (CommonCodeReg)commonCodeRegService.modify(cmcdRegRequest,
			commonCodeReg.getItem().getId(), account);

		// approve
		commonCodeRegService.process(modify.getId(), ProcessType.APPROVED, account);

		// Then
		CommonCode commonCode = (CommonCode)commonCodeService.get(modify.getItem().getId());
		assertThat(commonCode.getBase().getCode()).isEqualTo(cmcdRegRequest.getBase().getCode());
		assertThat(commonCode.getBase().getCodeName()).isEqualTo(clb.getBase().getName());
	}

	@Test
	public void rejectModifyCmcd() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("phn");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// modify
		cmcdRegRequest.getBase().setCode("B01");
		Word clb = wordService.getByEng("addr");
		CommonCodeReg modify = (CommonCodeReg)commonCodeRegService.modify(cmcdRegRequest,
			commonCodeReg.getItem().getId(), account);

		// reject
		commonCodeRegService.process(modify.getId(), ProcessType.REJECTED, account);

		// Then
		CommonCode commonCode = (CommonCode)commonCodeService.get(modify.getItem().getId());
		assertThat(commonCode.getBase().getCode()).isNotEqualTo(cmcdRegRequest.getBase().getCode());
		assertThat(commonCode.getBase().getCodeName()).isEqualTo(word.getBase().getName());
	}

	@Test
	public void approveDeleteCommonCode() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("phn");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// delete
		CommonCodeReg delete = (CommonCodeReg)commonCodeRegService.delete(commonCodeReg.getItem().getId(), account);

		// approve
		CommonCodeReg process = (CommonCodeReg)commonCodeRegService.process(delete.getId(), ProcessType.APPROVED,
			account);

		// Then
		assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
		assertThat(commonCodeRepository.count()).isEqualTo(1L);
		assertThat(process.getItem().getStatus()).isEqualTo(ProcessType.DELETABLE);
		commonCodeRegService.executeDelete(process.getId(), account);
	}

	@Test
	public void rejectDeleteCommonCode() throws Throwable {
		// When
		// create
		Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
		Word word = wordService.getByEng("addr");
		Account account = accountService.getAccount("test@test.com");
		CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(word), dict);
		CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);
		commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);

		// delete
		CommonCodeReg delete = (CommonCodeReg)commonCodeRegService.delete(commonCodeReg.getItem().getId(), account);

		// reject
		commonCodeRegService.process(delete.getId(), ProcessType.REJECTED, account);

		// Then
		assertThat(commonCodeRegRepository.count()).isEqualTo(2L);
		assertThat(commonCodeRepository.count()).isEqualTo(1L);

		CommonCodeReg rejectedReg = (CommonCodeReg)commonCodeRegService.getRegistration(delete.getId());
		assertThat(rejectedReg.getProcessType()).isEqualTo(ProcessType.REJECTED);
	}

	public CmcdRegRequest generateCmcdRegRequest(List<Word> words, Dict dict) {
		return CmcdRegRequest.builder()
			.base(generateCommonCodeBase())
			.dict(dict.getId())
			.build();
	}

	private CommonCodeBase generateCommonCodeBase() {
		return CommonCodeBase.builder()
			.code("AO1")
			.description("this is test cmcd descritption")
			.build();
	}
}