package insuranceCompany.application.domain.dao;

public interface CrudInterface<E> {
    void create(E e);
    E read(int id);
    boolean update(int id);
    boolean delete(int id);
}
