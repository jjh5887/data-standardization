package kvoting.intern.flowerwebapp.item;

public interface ItemService<I> {
    I get(Long id);

    I save(I item);

    void delete(I item);

    void delete(Long id);

}
