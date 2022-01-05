package kvoting.intern.flowerwebapp.item;

public interface ItemService {
	Item get(Long id);

	Item create(Item item);

	void delete(Item item);

	void delete(Long id);

	boolean exists(Item item);
}
