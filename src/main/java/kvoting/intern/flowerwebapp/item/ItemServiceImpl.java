package kvoting.intern.flowerwebapp.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    protected final JpaRepository itemRepository;

    @Transactional(readOnly = true)
    public Item get(Long id) throws Throwable {
        return (Item) itemRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public Item save(Item item) {
        return (Item) itemRepository.save(item);
    }

    public void delete(Item item) throws Throwable {
        itemRepository.delete(item);
    }

    public void delete(Long id) throws Throwable {
        itemRepository.deleteById(id);
    }

}
