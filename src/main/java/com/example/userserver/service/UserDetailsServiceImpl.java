package com.example.userserver.service;


import com.example.userserver.common.MessageUtils;
import com.example.userserver.entity.User;
import com.example.userserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User acc = userRepository.findByUserName(userName);
		Assert.notNull(acc, MessageUtils.getMessage("error.notfound", userName));
		return UserDetailsImpl.build(acc);
	}

}
