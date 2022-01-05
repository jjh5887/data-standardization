package kvoting.intern.flowerwebapp.dict.registeration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomain;
import kvoting.intern.flowerwebapp.ctdomain.CustomDomainService;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.dict.registeration.request.DictRegistRequest;
import kvoting.intern.flowerwebapp.domain.Domain;
import kvoting.intern.flowerwebapp.domain.DomainService;
import kvoting.intern.flowerwebapp.exception.WebException;
import kvoting.intern.flowerwebapp.exception.code.RegistrationErrorCode;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordService;

@Service
public class DictRegService extends RegistrationService {
	private final WordService wordService;
	private final DomainService domainService;
	private final CustomDomainService customDomainService;
	private final CommonCodeService commonCodeService;

	public DictRegService(DictRegRepository dictRegRepository, ModelMapper modelMapper, DictService dictService,
		@Lazy WordService wordService,
		@Lazy DomainService domainService,
		@Lazy CustomDomainService customDomainService,
		@Lazy CommonCodeService commonCodeService,
		AccountService accountService) {
		super(dictRegRepository, modelMapper, dictService, accountService);
		this.wordService = wordService;
		this.domainService = domainService;
		this.customDomainService = customDomainService;
		this.commonCodeService = commonCodeService;
		this.regClazz = DictReg.class;
		this.itemClazz = Dict.class;
	}

	@Override
	public void setUpItem(Item item, RegRequest regRequest) {
		DictRegistRequest request = (DictRegistRequest)regRequest;
		Dict dict = (Dict)item;
		dict.getBase().setIsCommon(false);
		dict.setWords(request.getWords().stream().
			map(id -> (Word)wordService.get(id))
			.collect(Collectors.toList()));
		dict.setDomains(request.getDomains().stream()
			.map(id -> (Domain)domainService.get(id))
			.collect(Collectors.toSet()));
		dict.setCustomDomains(request.getCustomDomains().stream()
			.map(id -> (CustomDomain)customDomainService.get(id))
			.collect(Collectors.toSet()));
		dict.setCommonCodes(request.getCommonCodes().stream()
			.map(id -> (CommonCode)commonCodeService.get(id))
			.collect(Collectors.toList()));
		dict.getBase().setIsCommon(true);
		if (dict.getCommonCodes().size() == 0) {
			dict.getBase().setIsCommon(false);
		}
	}

	@Override
	public void setUpReg(Registration registration, RegRequest regRequest) {
		DictReg dictReg = (DictReg)registration;
		DictRegistRequest request = (DictRegistRequest)regRequest;
		request.getBase().setIsCommon(false);
		dictReg.setWords(request.getWords().stream()
			.map(id -> (Word)wordService.get(id))
			.collect(Collectors.toList()));
		dictReg.setDomains(request.getDomains().stream()
			.map(id -> (Domain)domainService.get(id))
			.collect(Collectors.toSet()));
		dictReg.setCustomDomains(request.getCustomDomains().stream()
			.map(id -> (CustomDomain)customDomainService.get(id))
			.collect(Collectors.toSet()));
		dictReg.setCommonCodes(request.getCommonCodes().stream()
			.map(id -> (CommonCode)commonCodeService.get(id))
			.collect(Collectors.toList()));
		dictReg.getBase().setIsCommon(true);
		if (dictReg.getCommonCodes().size() == 0) {
			dictReg.getBase().setIsCommon(false);
		}
	}

	@Override
	public Registration getRegDetail(Long id) {
		DictReg registration = (DictReg)getRegistration(id);
		Hibernate.initialize(registration.getWords());
		Hibernate.initialize(registration.getDomains());
		Hibernate.initialize(registration.getCustomDomains());
		Hibernate.initialize(registration.getCommonCodes());
		return registration;
	}

	@Override
	public void update(Registration registration, Item item) {
		Dict dict = (Dict)item;
		DictReg dictReg = (DictReg)registration;
		dict.getWords().clear();
		dict.getWords().addAll(dictReg.getWords());
		if (dict.getDomains().size() + dict.getCustomDomains().size() == 0) {
			throw new WebException(RegistrationErrorCode.DictNeedDomain);
		}
		dict.getDomains().clear();
		dict.getDomains().addAll(dictReg.getDomains());
		dict.getCustomDomains().clear();
		dict.getCustomDomains().addAll(dictReg.getCustomDomains());
		dict.getCommonCodes().clear();
		dict.getCommonCodes().addAll(dictReg.getCommonCodes());
		dict.setBase(dictReg.getBase());
	}

	@Override
	@Transactional(readOnly = true)
	public void validateItem(Item item) {
		Dict dict = (Dict)item;
		List<Item> list = new ArrayList<>(dict.getWords());
		list.addAll(dict.getDomains());
		list.addAll(dict.getCustomDomains());
		list.addAll(dict.getCommonCodes());
		if (list.stream().anyMatch(i -> ProcessType.APPROVED != i.getStatus())) {
			throw new WebException(RegistrationErrorCode.NotApprovedItemExists);
		}
	}
}
