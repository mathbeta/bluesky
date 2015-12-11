package com.dc.storage.console.dao;

import com.dc.storage.console.bean.User;

public interface IUserDao {

	User validate(String userName, String password);

}
