package kvoting.intern.flowerwebapp.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
@Transactional
public abstract class ItemServiceImpl implements ItemService {
	protected final JpaRepository itemRepository;

	@SneakyThrows
	@Transactional(readOnly = true)
	public Item get(Long id) {
		return (Item)itemRepository.findById(id).orElseThrow(() -> {
			throw new RuntimeException();
		});
	}

	@Transactional(readOnly = true)
	public abstract Item getDetail(Long id);

	@Transactional(readOnly = true)
	public Page<Item> getAllItems(Pageable pageable) {
		return itemRepository.findAll(pageable);
	}

	public Item save(Item item) {
		return (Item)itemRepository.save(item);
	}

	public void delete(Item item) {
		itemRepository.delete(item);
	}

	public void delete(Long id) throws Throwable {
		itemRepository.deleteById(id);
	}
}
