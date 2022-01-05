package kvoting.intern.flowerwebapp.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kvoting.intern.flowerwebapp.exception.WebException;
import kvoting.intern.flowerwebapp.exception.code.ItemErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
@Transactional
public abstract class ItemServiceImpl implements ItemService {
	protected final JpaRepository itemRepository;

	@Override
	@SneakyThrows
	@Transactional(readOnly = true)
	public Item get(Long id) {
		return (Item)itemRepository.findById(id).orElseThrow(() -> {
			throw new WebException(ItemErrorCode.ItemNotFound);
		});
	}

	@Transactional(readOnly = true)
	public abstract Item getDetail(Long id);

	@Transactional(readOnly = true)
	public Page<Item> getAllItems(Pageable pageable) {
		return itemRepository.findAll(pageable);
	}

	@Override
	public Item create(Item item) {
		if (exists(item)) {
			throw new WebException(ItemErrorCode.DuplicatedItem);
		}
		return (Item)itemRepository.save(item);
	}

	public Item update(Item item) {
		return (Item)itemRepository.save(item);
	}

	@Override
	public void delete(Item item) {
		itemRepository.delete(item);
	}

	@Override
	public void delete(Long id) {
		itemRepository.deleteById(id);
	}
}
