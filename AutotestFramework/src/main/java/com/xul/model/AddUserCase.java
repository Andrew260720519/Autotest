package com.xul.model;

import lombok.Data;

@Data
public class AddUserCase {
    private String userName;
    private String password;
    private String age;
    private String sex;
    private String permission;
    private String isDelete;
    private String expected;
}
