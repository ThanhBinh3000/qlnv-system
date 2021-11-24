package com.tcdt.qlnvsystem.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigValue {
	/*
	 * email username
	 */
	@Value("${email.user}")
	public String user;

	/*
	 * password sendmail
	 */
	@Value("${email.password}")
	public String password;
}
