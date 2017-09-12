package cn.forwe.sgk;

/**
 * Created by Jonathan.X.J.He on 2017/8/3.
 */
public class QgroupInfo {
    private Long id;
    private String qqNum;
    private String nick;
    private Integer age;
    private Integer gender;
    private String auth;
    private String qunNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getQunNum() {
        return qunNum;
    }

    public void setQunNum(String qunNum) {
        this.qunNum = qunNum;
    }
}
