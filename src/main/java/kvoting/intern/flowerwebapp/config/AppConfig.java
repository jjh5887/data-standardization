package kvoting.intern.flowerwebapp.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kvoting.intern.flowerwebapp.account.Account;
import kvoting.intern.flowerwebapp.account.AccountRole;
import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.account.request.AccountCreateRequest;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeBase;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeReg;
import kvoting.intern.flowerwebapp.cmcd.registration.CommonCodeRegService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.constraint.ConstraintBase;
import kvoting.intern.flowerwebapp.constraint.ConstraintService;
import kvoting.intern.flowerwebapp.constraint.InputType;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintReg;
import kvoting.intern.flowerwebapp.constraint.registration.ConstraintRegService;
import kvoting.intern.flowerwebapp.constraint.registration.request.ConstraintRegRequest;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainBase;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.ctdomain.registration.CustomDomainReg;
import kvoting.intern.flowerwebapp.ctdomain.registration.CustomDomainRegService;
import kvoting.intern.flowerwebapp.ctdomain.registration.request.CustomDomainRegRequest;
import kvoting.intern.flowerwebapp.dict.CaseStyle;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictBase;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.DictReg;
import kvoting.intern.flowerwebapp.dict.registeration.DictRegService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.DB;
import kvoting.intern.flowerwebapp.domain.DataType;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainBase;
import kvoting.intern.flowerwebapp.domain.DomainRepository;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.domain.registration.DomainReg;
import kvoting.intern.flowerwebapp.domain.registration.DomainRegService;
import kvoting.intern.flowerwebapp.domain.registration.request.DomainRegistRequest;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationRepository;
import kvoting.intern.flowerwebapp.jwt.JwtTokenProvider;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordRepository;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.WordReg;
import kvoting.intern.flowerwebapp.word.registration.WordRegService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import lombok.SneakyThrows;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	@Bean
	public ObjectMapper objectMapper() {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		return new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.registerModule(new Hibernate5Module())
			.setDateFormat(df);
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {

			@Autowired
			RegistrationRepository registrationRepository;

			@Autowired
			WordRegService wordRegService;

			@Autowired
			AccountService accountService;

			@Autowired
			DomainRegService domainRegService;

			@Autowired
			WordService wordService;

			@Autowired
			WordRepository wordRepository;

			@Autowired
			DomainRepository domainRepository;

			@Autowired
			DomainService domainService;

			@Autowired
			DictService dictService;

			@Autowired
			DictRegService dictRegService;

			@Autowired
			CommonCodeRegService commonCodeRegService;

			@Autowired
			ConstraintRegService constraintRegService;

			@Autowired
			CustomDomainRegService customDomainRegService;

			@Autowired
			CustomDomainService customDomainService;

			@Autowired
			ConstraintService constraintService;

			@Autowired
			JwtTokenProvider jwtTokenProvider;

			public AccountCreateRequest generateCreateRequest(String email, String password, String name,
				String department) {
				return AccountCreateRequest.builder()
					.email(email)
					.password(password)
					.name(name)
					.department(department)
					.build();
			}

			private WordRegistRequest generateWordRequest(String engName, String name, String orgEngName) {
				return WordRegistRequest.builder()
					.base(WordBase.builder()
						.engName(engName)
						.name(name)
						.orgEngName(orgEngName)
						.build())
					.build();
			}

			private DomainRegistRequest generateDomainRequest(int idx, List<Long> ids) {
				return DomainRegistRequest.builder()
					.base(generateDomainBase(idx))
					.words(ids)
					.build();
			}

			private DomainBase generateDomainBase(int idx) {
				return DomainBase.builder()
					.size(20)
					.nullable(true)
					.description("this is test domain")
					.db(DB.ORACLE)
					.dataType(DataType.values()[idx])
					.build();
			}

			private DictRegistRequest generateDictRequest(List<Long> words, CaseStyle cs, String nm,
				Set<Long> domains) {
				return DictRegistRequest.builder()
					.words(words)
					.base(generateDictBase(nm, cs))
					.domains(domains)
					.customDomains(new HashSet<>())
					.build();
			}

			private DictBase generateDictBase(String snm, CaseStyle cs) {
				return DictBase.builder()
					.screenName(snm)
					.isCommon(false)
					.caseStyle(cs)
					.build();
			}

			public CmcdRegRequest generateCmcdRegRequest(List<Word> words, Dict dict) {
				return CmcdRegRequest.builder()
					.base(generateCommonCodeBase())
					.dict(dict.getId())
					.build();
			}

			private CommonCodeBase generateCommonCodeBase() {
				return CommonCodeBase.builder()
					.code("TO1")
					.codeName("테스트 공통 코드")
					.description("this is test cmcd descritption")
					.build();
			}

			private ConstraintRegRequest generateConstraintRegRequest() {
				return ConstraintRegRequest.builder()
					.base(generateConstraintBase())
					.build();
			}

			private ConstraintBase generateConstraintBase() {
				return ConstraintBase.builder()
					.description("K를 붙일 수 있는가?")
					.inputType(InputType.BOOLEAN)
					.value("0")
					.name("Kable")
					.build();
			}

			private CustomDomainRegRequest generateCustomDomainRegRequest(Set<Long> constraints, List<Long> words) {
				return CustomDomainRegRequest.builder()
					.base(generateCustomDomainBase())
					.constraints(constraints)
					.words(words)
					.build();
			}

			private CustomDomainBase generateCustomDomainBase() {
				return CustomDomainBase.builder()
					.db("KDB")
					.dataType("KVARCHAR")
					.description("K 디비에서 쓰이는 문자열")
					.build();
			}

			@SneakyThrows
			@Override
			public void run(ApplicationArguments args) {
				AccountCreateRequest accountCreateRequest = generateCreateRequest("test@test.com", "1234", "관리자",
					"연구소");
				AccountCreateRequest suminReq = generateCreateRequest("tnals2322", "1234", "이수민", "연구소");
				AccountCreateRequest kihoonReq = generateCreateRequest("kihoon0510", "1234", "남기훈", "연구소");
				Account savedAccount = accountService.create(accountCreateRequest);
				accountService.create(suminReq);
				accountService.create(kihoonReq);
				savedAccount.addRole(AccountRole.ADMIN);
				Account account = accountService.save(savedAccount);

				List<WordRegistRequest> requests = new ArrayList<>();
				requests.add(generateWordRequest("shhd", "주주", "shareholder"));
				requests.add(generateWordRequest("gemt", "총회", "general meeting"));
				requests.add(generateWordRequest("phnm", "핸드폰번호", "phone number"));
				requests.add(generateWordRequest("dt", "날짜", "date"));
				requests.add(generateWordRequest("num", "숫자", "number"));
				requests.add(generateWordRequest("vt", "투표", "vote"));
				requests.add(generateWordRequest("plac", "장소", "place"));
				requests.add(generateWordRequest("addr", "주소", "address"));

				requests.add(generateWordRequest("us", "사용자", "user"));
				requests.add(generateWordRequest("pass", "비밀번호", "password"));
				requests.add(generateWordRequest("nm", "이름", "name"));
				requests.add(generateWordRequest("mb", "사용자", "member"));
				requests.add(generateWordRequest("desc", "설명", "description"));

				List<WordReg> wordRegs = new ArrayList<>();
				for (WordRegistRequest request : requests) {
					wordRegs.add((WordReg)wordRegService.create(request, account));
				}

				WordReg wordReg = wordRegs.get(0);
				WordRegistRequest request = requests.get(0);

				wordRegService.process(wordReg.getId(), ProcessType.APPROVED, account);
				request.getBase().setEngName("shd");

				WordReg modify = (WordReg)wordRegService.modify(request, wordReg.getItem().getId(), account);
				wordRegService.process(modify.getId(), ProcessType.APPROVED, account);

				for (WordReg reg : wordRegs) {
					wordRegService.process(reg.getId(), ProcessType.APPROVED, account);
				}

				Word pass = wordService.getByEng("pass");
				Word us = wordService.getByEng("us");
				Word nm = wordService.getByEng("nm");
				Word mb = wordService.getByEng("mb");
				List<Word> words = wordRepository.findAll();
				List<DomainReg> domainRegs = new ArrayList<>();
				for (int i = 0; i < DataType.values().length; i++) {
					domainRegs.add((DomainReg)domainRegService
						.create(generateDomainRequest(i, List.of(us.getId(), pass.getId())), account));
				}

				for (int i = 0; i < domainRegs.size(); i++) {
					domainRegService.process(domainRegs.get(i).getId(), ProcessType.APPROVED, account);
				}

				Word word = (Word)wordService.get(words.get(2).getId());
				word.getBase().setEngName("phn");
				wordService.save(word);

				// getDomains
				Page<Domain> varDomains = domainService.getByEngNameContains("VA", PageRequest.of(0, 10));
				System.out.println();
				Domain var = domainRepository.findAll().get(0);
				Page<Domain> numDomains = domainService.getByEngNameContains("NUM", PageRequest.of(0, 10));
				Domain num = domainRepository.findAll().get(1);

				// create dict_reg
				DictRegistRequest dictRegistRequest = generateDictRequest(List.of(us.getId(), pass.getId()),
					CaseStyle.CAMEL, "비밀번호", Set.of(var.getId()));
				dictRegService.create(dictRegistRequest, account);
				DictRegistRequest mb_nm = generateDictRequest(List.of(mb.getId(), nm.getId()), CaseStyle.SNAKE, "회원 이름",
					Set.of(num.getId()));
				DictReg reg = (DictReg)dictRegService.create(mb_nm, account);
				Dict item = reg.getItem();

				Dict dict = dictService.getDictByEngName("MB_NM", PageRequest.of(0, 10)).getContent().get(0);
				Word blb = wordService.getByEng("addr");
				CmcdRegRequest cmcdRegRequest = generateCmcdRegRequest(List.of(blb), dict);
				CommonCodeReg commonCodeReg = (CommonCodeReg)commonCodeRegService.create(cmcdRegRequest, account);

				// approve
				dictRegService.process(reg.getId(), ProcessType.APPROVED, account);
				commonCodeRegService.process(commonCodeReg.getId(), ProcessType.APPROVED, account);
				ConstraintReg constraintReg = (ConstraintReg)constraintRegService.create(generateConstraintRegRequest(),
					account);
				CustomDomainReg customDomainReg = (CustomDomainReg)customDomainRegService.create(
					generateCustomDomainRegRequest(Set.of(constraintReg.getItem().getId()),
						List.of(us.getId(), pass.getId())), account);

				CustomDomain customDomain = customDomainService.getDetail(customDomainReg.getItem().getId());
				constraintRegService.process(constraintReg.getId(), ProcessType.APPROVED, account);
				customDomainRegService.process(customDomainReg.getId(), ProcessType.APPROVED, account);
				mb_nm.getCustomDomains().add(customDomain.getId());
				DictReg modifyDictReg = (DictReg)dictRegService.modify(mb_nm, item.getId(), account);
				dictRegService.process(modifyDictReg.getId(), ProcessType.APPROVED, account);

				WordRegistRequest typeWord = generateWordRequest("tp", "타입", "Type");
				WordRegistRequest codeWord = generateWordRequest("cd", "코드", "Code");
				WordReg typeReg = (WordReg)wordRegService.create(typeWord, account);
				WordReg codeReg = (WordReg)wordRegService.create(codeWord, account);

				DictRegistRequest processTypeRequest = generateDictRequest(
					List.of(typeReg.getItemId(), codeReg.getItemId()), CaseStyle.SNAKE, "처리 상태", Set.of(var.getId()));
				DictReg dictReg = (DictReg)dictRegService.create(processTypeRequest, account);
				CmcdRegRequest tpcdRequest = generateCmcdRegRequest(null, dictReg.getItem());
				tpcdRequest.getBase().setCode("A01");
				tpcdRequest.getBase().setCodeName("처리 상태 코드");
				tpcdRequest.getBase().setDescription("처리 상태를 나타내는 코드");
				CommonCodeReg tpcdReg = (CommonCodeReg)commonCodeRegService.create(tpcdRequest, account);

				CmcdRegRequest approveRequest = generateCmcdRegRequest(null, dictReg.getItem());
				approveRequest.setDict(null);
				approveRequest.getBase().setCode("A0101");
				approveRequest.getBase().setCodeName("승인");
				approveRequest.getBase().setDescription("승인됨");
				approveRequest.getBase().setOrder(0L);
				approveRequest.setHighCommonCode(tpcdReg.getItemId());
				approveRequest.setHighDict(tpcdReg.getDictId());
				commonCodeRegService.create(approveRequest, account);

				approveRequest.getBase().setCode("A0102");
				approveRequest.getBase().setCodeName("거절");
				approveRequest.getBase().setDescription("거절됨");
				approveRequest.getBase().setOrder(1L);
				approveRequest.setHighCommonCode(tpcdReg.getItemId());
				approveRequest.setHighDict(tpcdReg.getDictId());
				commonCodeRegService.create(approveRequest, account);
				WordRegistRequest request1 = generateWordRequest(us.getBase().getEngName(), us.getBase().getName(),
					us.getBase().getOrgEngName());
				request1.getBase().setName("유저");
				request1.getBase().setEngName("ac");
				Registration registration = wordRegService.modify(request1, us.getId(), account);
				wordRegService.process(registration.getId(), ProcessType.APPROVED, account);
				System.out.println(jwtTokenProvider.createToken("test@test.com"));
			}
		};
	}
}
