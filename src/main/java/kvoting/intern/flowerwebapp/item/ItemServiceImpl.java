package kvoting.intern.flowerwebapp.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl<I extends Item> implements ItemService<I> {
    protected final JpaRepository<I, Long> itemRepository;

    @Transactional(readOnly = true)
    public I get(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public I save(I item) {
        return itemRepository.save(item);
    }

    public void delete(I item) {
        itemRepository.delete(item);
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

}
