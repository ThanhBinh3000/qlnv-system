package com.tcdt.qlnvsystem.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class addChildListReq extends BaseRequest {
	@NotNull(message = "Không được để trống")
	Long id;
	@NotNull(message = "Không được để trống")
	List<Long> childId;
}
