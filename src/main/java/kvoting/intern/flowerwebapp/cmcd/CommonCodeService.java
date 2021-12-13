package kvoting.intern.flowerwebapp.cmcd;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonCodeService {
    private final CommonCodeRepository commonCodeRepository;

    public CommonCode save(CommonCode commonCode) {
        return commonCodeRepository.save(commonCode);
    }

    public CommonCode getCommonCode(Long id) {
        return commonCodeRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public void delete(Long id) {
        commonCodeRepository.deleteById(id);
    }
}
