package com.mammon.auth.domain;

import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetail {
    private String id;
    private long merchantNo;
    private long storeNo;
    private String username;
    private String phone;
    private String password;
    private int status;
    private int merchantStatus;
    private int storeStatus;
    private LocalDate storeExpireDate;
    private List<String> perms;
    private Date lastPasswordResetDate;
}
