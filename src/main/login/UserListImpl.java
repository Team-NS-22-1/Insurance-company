package main.login;

import main.domain.insurance.Insurance;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserListImpl implements UserList {

    private HashMap<Integer, User> userList = new HashMap<>();
    private static int id = 0;

    public UserListImpl(){
        testDummyUser();
    }

    public ArrayList<User> getUserListToArrayList() {
        return new ArrayList<>(this.userList.values());
    }

    void testDummyUser() {
        this.create(new User()
                        .setUserId("seungho")
                        .setPassword("qwer1234")
                        .setCustomerId(-1)
                        .setEmployeeId(1));
        this.create(new User()
                        .setUserId("test1")
                        .setPassword("test1")
                        .setCustomerId(1)
                        .setEmployeeId(-1));
    }

    @Override
    public void create(User user) {
        this.userList.put(user.setId(id).getId(), user);
    }

    @Override
    public User read(int id) {
        User user = this.userList.get(id);
        if(user != null) return user;
        else return null;
    }

    @Override
    public boolean delete(int id) {
        User user = this.userList.remove(id);
        if(user != null) return true;
        else return false;
    }
}
