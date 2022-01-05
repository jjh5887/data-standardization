package kvoting.intern.flowerwebapp.cmcd;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.ItemServiceImpl;

@Service
public class CommonCodeService extends ItemServiceImpl {

	public CommonCodeService(CommonCodeRepository commonCodeRepository) {
		super(commonCodeRepository);
	}

	@Override
	@Transactional(readOnly = true)
	public CommonCode getDetail(Long id) {
		CommonCode commonCode = (CommonCode)get(id);
		Hibernate.initialize(commonCode.getDicts());
		// Hibernate.initialize(commonCode.getHighCommonCode());
		// Hibernate.initialize(commonCode.getLowCommonCodes());
		return commonCode;
	}

	@Transactional(readOnly = true)
	public Page<CommonCode> getByCode(String code, Pageable pageable) {
		return ((CommonCodeRepository)itemRepository).findByBase_CodeIgnoreCaseContains(code, pageable);
	}

	@Transactional(readOnly = true)
	public Page<CommonCode> getByCodeName(String codeName, Pageable pageable) {
		return ((CommonCodeRepository)itemRepository).findByBase_CodeNameIgnoreCaseContains(codeName,
			pageable);
	}

	// @Transactional(readOnly = true)
	// public Page<CommonCode> getByHighCmcd(Long id, Pageable pageable) {
	// 	return ((CommonCodeRepository)itemRepository).findByHighCommonCodeIdOrderByBase_Order(id,
	// 		pageable);
	// }

	@Override
	public boolean exists(Item item) {
		CommonCode commonCode = (CommonCode)item;
		return ((CommonCodeRepository)itemRepository)
			.exists(commonCode.getBase().getCode(),
				commonCode.getBase().getCodeName());
	}
}
