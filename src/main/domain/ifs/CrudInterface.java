package main.domain.ifs;

public interface CrudInterface<E> {
    boolean create(E e);
    E read(int id);
//    boolean update(int id);
    boolean delete(int id);
}
