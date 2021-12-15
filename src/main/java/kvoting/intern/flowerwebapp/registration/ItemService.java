package kvoting.intern.flowerwebapp.registration;

public interface ItemService<I, R> {
    public I get(Long id);

    public I map(R r);

    public I save(I item);

    public void delete(I item);

    public void setStatus(I item, ProcessType type);
}
