package com.dc.storage.console.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.dc.storage.console.bean.User;
import com.dc.storage.console.dao.IUserDao;

@Component("userDao")
public class UserDao extends SqlSessionDaoSupport implements IUserDao {
	@Autowired
	public UserDao(
			@Qualifier("sqlSession") SqlSessionTemplate sqlSessionTemplate) {
		super();
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public User validate(String userName, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("password", password);
		return getSqlSession().selectOne("user.validate", params);
	}

}
