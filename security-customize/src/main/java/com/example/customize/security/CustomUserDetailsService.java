package com.example.customize.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.customize.UserEntity;
import com.xuanmo.framework.core.common.Md5;

public class CustomUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserEntity user = loadUserBySql(username);

		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : user.getRoles()) {
			// Spring security GrantedAuthority 默认以 ROLE_ 开头
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}

		return new User(user.getUsername(), user.getPassword(), authorities);
	}

	// 模拟服务层方法从数据库取出 user
	public UserEntity loadUserBySql(String username) {
		if (!username.equals("maiyo"))
			return null;

		UserEntity user = new UserEntity();
		user.setId(1);
		user.setUsername("maiyo");
		user.setPassword(Md5.MD5Encode("pass"));
		user.setIsDisable(false);
		user.setLastLoginTime(new Date());

		Set<String> roles = new HashSet<String>();
		roles.add("USER");

		user.setRoles(roles);
		return user;
	}

}