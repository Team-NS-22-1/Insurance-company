package insuranceCompany.application.viewlogic.dto.UserDto;

public class UserDto {
    private String userId;
    private String password;
    private int roleId;

    public UserDto(String userId, String password, int roleId){
        this.userId = userId;
        this.password = password;
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public int getRoleId() {
        return roleId;
    }
}
