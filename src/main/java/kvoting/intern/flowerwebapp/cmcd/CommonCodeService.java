package kvoting.intern.flowerwebapp.cmcd;

import kvoting.intern.flowerwebapp.cmcd.registration.request.CmcdRegRequest;
import kvoting.intern.flowerwebapp.registration.ItemService;
import kvoting.intern.flowerwebapp.registration.ProcessType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonCodeService implements ItemService<CommonCode, CmcdRegRequest> {
    private final CommonCodeRepository commonCodeRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public CommonCode get(Long id) {
        return commonCodeRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    @Override
    public CommonCode map(CmcdRegRequest request) {
        CommonCode map = modelMapper.map(request, CommonCode.class);
        map.setStatus(ProcessType.UNHANDLED);
        map.setCommonCodeRegs(new HashSet<>());
        return map;
    }

    @Override
    public CommonCode save(CommonCode commonCode) {
        return commonCodeRepository.save(commonCode);
    }

    @Override
    public void delete(CommonCode item) {
        commonCodeRepository.delete(item);
    }

    @Override
    public void setStatus(CommonCode item, ProcessType type) {
        item.setStatus(type);
    }

    public void delete(Long id) {
        commonCodeRepository.deleteById(id);
    }
}
