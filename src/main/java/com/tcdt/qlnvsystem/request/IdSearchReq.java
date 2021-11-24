package com.tcdt.qlnvsystem.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class IdSearchReq {
	@NotNull(message = "Không được để trống")
	Long id;
}
