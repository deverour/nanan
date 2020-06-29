package com.tower.nanan.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RebackQueryBean implements Serializable {
	private Integer currentPage;//页码
	private Integer pageSize;//每页记录数

	private List<String> region;
	private List<String> customer;
	private String rebacked;
	private String rebackCode;
	private String startRebackDate;
	private String endRebackDate;
	private String startAccountPeriod;
	private String endAccountPeriod;

}