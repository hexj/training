package forwe.hexj.learn.mybatis.servcie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import forwe.hexj.learn.mybatis.dao.UserMapper;
import forwe.hexj.learn.mybatis.model.User;

@Service("userService")
public class UserServiceImpl implements IUserService {

	private UserMapper userMapper;

	public UserMapper getUserMapper() {
		return userMapper;
	}

	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public User getUserById(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

}
