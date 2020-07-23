package com.tower.nanan.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "name_code")
public class NameCode {
    @Id
    private String siteCode;
    private String siteName;
}
