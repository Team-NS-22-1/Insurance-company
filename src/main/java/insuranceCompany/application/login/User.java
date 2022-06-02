package insuranceCompany.application.login;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String userId;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private int roleId;

    public User(){
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public User setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public User setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
        return this;
    }

    public int getRoleId() {
        return roleId;
    }

    public User setRoleId(int role_id) {
        this.roleId = role_id;
        return this;
    }
}
