package com.mathbeta.storage.console.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mathbeta.storage.console.bean.User;
import com.mathbeta.storage.console.dao.IUserDao;
import com.mathbeta.storage.console.service.IUserService;

@Service("userService")
public class UserService implements IUserService {
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;

	@Override
	public User validate(String userName, String password) {
		return userDao.validate(userName, password);
	}

}
