package kvoting.intern.flowerwebapp.item;

public interface ItemService {
	Item get(Long id) throws Throwable;

	Item save(Item item);

	void delete(Item item) throws Throwable;

	void delete(Long id) throws Throwable;
}
