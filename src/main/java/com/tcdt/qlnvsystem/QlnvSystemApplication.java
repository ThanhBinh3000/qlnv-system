package com.tcdt.qlnvsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = { "com.tcdt.qlnvsystem.entities", "com.tcdt.qlnvsystem.table" })
@EnableFeignClients(basePackages = "com.tcdt.qlnvsystem.service.feign")
@EnableEurekaClient
public class QlnvSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(QlnvSystemApplication.class, args);
	}

}
