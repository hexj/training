package forwe.hexj.learn.mybatis.servcie;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import forwe.hexj.learn.mybatis.dao.UserMapper;
import forwe.hexj.learn.mybatis.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/spring.xml",
		"classpath:conf/spring-mybatis.xml" })
public class UserServiceImplTest {
	private static final Logger logger = Logger
			.getLogger(UserServiceImplTest.class);

	private IUserService userService;
	private UserMapper mapper;

	public IUserService getUserSerivce() {
		return userService;
	}

	@Autowired
	public void setUserSerivce(IUserService userSerivce) {
		this.userService = userSerivce;
	}

	@Test
	public void getUserByIdtest() {
		User u = userService.getUserById(1);
		logger.info(JSON.toJSONStringWithDateFormat(u, "yyyy-MM-dd HH:mm:ss"));
	}

	@Test
	public void getUserByIdtest2() {
		User u = mapper.selectByPrimaryKey(2);
		logger.info(JSON.toJSONStringWithDateFormat(u, "yyyy-MM-dd HH:mm:ss"));
	}

	public UserMapper getMapper() {
		return mapper;
	}

	@Autowired
	public void setMapper(UserMapper mapper) {
		this.mapper = mapper;
	}

}
