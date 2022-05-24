package login;

import java.util.StringTokenizer;

public class User {

    int id;

    String userId;

    String password;

    int customerId;

    int employeeId;

    public User(){
    }

    public User(String inputString){
        StringTokenizer stn = new StringTokenizer(inputString, "'");
        this.id = Integer.parseInt(stn.nextToken());
        this.userId = stn.nextToken();
        this.password = stn.nextToken();
        this.customerId = Integer.parseInt(stn.nextToken());
        this.employeeId = Integer.parseInt(stn.nextToken());
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getCustomerId() {
        return customerId;
    }

    public User setCustomerId(int customerId) {
        this.customerId = customerId;
        return this;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public User setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
        return this;
    }
}
