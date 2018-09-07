package cn.work.service;

import cn.work.dao.RegistDao;
import cn.work.domain.Admin;
import cn.work.domain.User;
import cn.work.utils.MD5Utils;

public class UserService {

	public boolean regist(User user) {
		//对密码进行MD5加密
		user.setPassword(MD5Utils.md5(user.getPassword())); 
		RegistDao dao = new RegistDao();
		return dao.regist(user);
	}

	public boolean active(String code) {
		RegistDao dao = new RegistDao();
		return dao.active(code);
	}

	public boolean checkUsername(String username) {
		RegistDao dao = new RegistDao();
		return dao.checkUsername(username);
	}

	
	public User login(User user) {
		// TODO Auto-generated method stub
		String password = user.getPassword();
		password = MD5Utils.md5(password);
		user.setPassword(password);
		user = new RegistDao().login(user);
		return user;
	}

	public User infoChangeService(User user) {
		RegistDao rd = new RegistDao();
		return rd.infoChangeDao(user);
		
	}

	public Admin adLogin(Admin admin) {
		// TODO Auto-generated method stub
		return new RegistDao().adLogin(admin);
	}

}
