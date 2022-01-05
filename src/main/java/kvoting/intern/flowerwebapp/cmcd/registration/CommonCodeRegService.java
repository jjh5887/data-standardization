package kvoting.intern.flowerwebapp.cmcd.registration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import kvoting.intern.flowerwebapp.account.AccountService;
import kvoting.intern.flowerwebapp.cmcd.CommonCode;
import kvoting.intern.flowerwebapp.cmcd.CommonCodeService;
import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.dict.Dict;
import kvoting.intern.flowerwebapp.dict.DictService;
import kvoting.intern.flowerwebapp.exception.WebException;
import kvoting.intern.flowerwebapp.exception.code.RegistrationErrorCode;
import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.ProcessType;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;

@Service
public class CommonCodeRegService extends RegistrationService {
	private final DictService dictService;

	public CommonCodeRegService(CommonCodeRegRepository commonCodeRegRepository,
		ModelMapper modelMapper, CommonCodeService commonCodeService,
		@Lazy DictService dictService, AccountService accountService) {
		super(commonCodeRegRepository, modelMapper, commonCodeService, accountService);
		this.dictService = dictService;
		this.regClazz = CommonCodeReg.class;
		this.itemClazz = CommonCode.class;
	}

	@Override
	public void validateItem(Item item) {
		// CommonCode commonCode = (CommonCode)item;
		// for (CommonCode lowCommonCode : commonCode.getLowCommonCodes()) {
		// 	if (lowCommonCode.getStatus() != ProcessType.APPROVED){
		// 		throw new WebException(RegistrationErrorCode.NotApprovedItemExists);
		// 	}
		// }
	}

	@Override
	public void setUpItem(Item item, RegRequest regRequest) {
		CmcdRegRequest request = (CmcdRegRequest)regRequest;
		CommonCode commonCode = (CommonCode)item;
		// if (request.getDict() != null) {
		// 	Dict dict = (Dict)dictService.get(request.getDict());
		// 	dict.getBase().setIsCommon(true);
		// 	commonCode.setDict(dict);
		// 	commonCode.setDictId(commonCode.getDict().getId());
		// 	commonCode.setDictName(commonCode.getDict().getName());
		// }
		// if (request.getHighDict() != null) {
		// 	commonCode.setHighDict((Dict)dictService.get(request.getHighDict()));
		// 	commonCode.setHighDictId(commonCode.getHighDict().getId());
		// 	commonCode.setHighDictName(commonCode.getHighDict().getName());
		// }
		// if (request.getHighCommonCode() != null) {
		// 	commonCode.setHighCommonCode(
		// 		(CommonCode)itemServiceImpl.get(request.getHighCommonCode()));
		// 	commonCode.setHighCmcdId(commonCode.getHighCommonCode().getId());
		// 	commonCode.setHighCmcdName(commonCode.getHighCommonCode().getName());
		// }
	}

	@Override
	public void setUpReg(Registration registration, RegRequest regRequest) {
		CmcdRegRequest request = (CmcdRegRequest)regRequest;
		CommonCodeReg commonCodeReg = (CommonCodeReg)registration;
		// if (request.getDict() != null) {
		// 	commonCodeReg.setDict((Dict)dictService.get(request.getDict()));
		// 	commonCodeReg.setDictId(commonCodeReg.getDict().getId());
		// 	commonCodeReg.setDictName(commonCodeReg.getDict().getName());
		// }
		// if (request.getHighDict() != null) {
		// 	commonCodeReg.setHighDict((Dict)dictService.get(request.getHighDict()));
		// 	commonCodeReg.setHighDictId(commonCodeReg.getHighDict().getId());
		// 	commonCodeReg.setHighDictName(commonCodeReg.getHighDict().getName());
		// }
		// if (request.getHighCommonCode() != null) {
		// 	commonCodeReg.setHighCommonCode(
		// 		(CommonCode)itemServiceImpl.get(request.getHighCommonCode()));
		// 	commonCodeReg.setHighCmcdId(commonCodeReg.getHighCommonCode().getId());
		// 	commonCodeReg.setHighCmcdName(commonCodeReg.getHighCommonCode().getName());
		// }
	}

	@Override
	public void update(Registration registration, Item item) {
		CommonCodeReg commonCodeReg = (CommonCodeReg)registration;
		CommonCode commonCode = (CommonCode)item;
		// commonCode.setDict(commonCodeReg.getDict());
		// commonCode.setHighDict(((CommonCodeReg)registration).getHighDict());
		// commonCode.setHighCommonCode(commonCodeReg.getHighCommonCode());
		// commonCode.setBase(commonCodeReg.getBase());
	}
}
