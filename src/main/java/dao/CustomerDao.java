package dao;

import domain.customer.Customer;

/**
 * packageName :  dao
 * fileName : CustomerDao
 * author :  규현
 * date : 2022-05-25
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022-05-25                규현             최초 생성
 */
public class CustomerDao extends Dao{

    public CustomerDao() {
        super();
        super.connect();
    }

    public void create(Customer customer) {
        String query = "insert into customer (name) values ('test')";
        int id = super.create(query);
        customer.setId(id);
    }
}
