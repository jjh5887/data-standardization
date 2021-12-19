package kvoting.intern.flowerwebapp.cmcd;

import kvoting.intern.flowerwebapp.item.ItemServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommonCodeService extends ItemServiceImpl<CommonCode> {
    public CommonCodeService(CommonCodeRepository commonCodeRepository) {
        super(commonCodeRepository);
    }
}
