package com.tower.nanan.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@ToString
@Getter
@Setter
public class User  {
    @Id
    private String id;
    private String username;
    private String password;
    private String region;
    private String ngroup;
    @Transient
    private String newpassword;


}