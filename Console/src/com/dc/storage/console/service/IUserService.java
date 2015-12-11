package com.dc.storage.console.service;

import com.dc.storage.console.bean.User;

public interface IUserService {

	User validate(String userName, String password);

}
