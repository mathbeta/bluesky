package com.mathbeta.storage.console.dao;

import com.mathbeta.storage.console.bean.User;

public interface IUserDao {

	User validate(String userName, String password);

}
