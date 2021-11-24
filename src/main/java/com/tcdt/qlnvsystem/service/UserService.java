package com.tcdt.qlnvsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvsystem.jwt.CustomUserDetails;
import com.tcdt.qlnvsystem.repository.UserActionRepository;
import com.tcdt.qlnvsystem.repository.UserHistoryRepository;
import com.tcdt.qlnvsystem.repository.UserInfoRepository;
import com.tcdt.qlnvsystem.table.UserAction;
import com.tcdt.qlnvsystem.table.UserHistory;
import com.tcdt.qlnvsystem.table.UserInfo;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserInfoRepository userRepository;
	@Autowired
	UserHistoryRepository userHistoryRepository;

	@Autowired
	UserActionRepository userActionRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user);
	}

	public Iterable<UserAction> findAll() {
		return userActionRepository.findAll();
	}

	public void saveUserHistory(UserHistory userHistory) {
		userHistoryRepository.save(userHistory);
	}

}