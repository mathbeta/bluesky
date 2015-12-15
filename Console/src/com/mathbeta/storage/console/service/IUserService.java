package com.mathbeta.storage.console.service;

import com.mathbeta.storage.console.bean.User;

public interface IUserService {

	User validate(String userName, String password);

}
