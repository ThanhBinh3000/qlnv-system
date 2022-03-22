package com.tcdt.qlnvsystem.jwt;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tcdt.qlnvsystem.table.UserInfo;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	UserInfo user;

	public CustomUserDetails(UserInfo user2) {
		this.user = user2;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	// mac dinh ko check password de la ""
	@Override
	public String getPassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode("");
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (user.getStatus().equals("01")) {
			return true;
		} else {
			return false;
		}
	}

	public String getDvql() {
		return user.getDvql();
	}

}
