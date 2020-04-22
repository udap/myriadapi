package io.chainmind.myriadapi.domain.dto;

import io.chainmind.myriadapi.domain.EmploymentStatus;
import io.chainmind.myriadapi.domain.Role;

import java.util.Date;

public class EmployeeResponse {

    private String uid;
    private String code;
    private String name;
    private String cellphone;
    private Date entryTime;
    private NamedEntityDTO org;

    // employment status
    private EmploymentStatus status;
    private Role role;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public NamedEntityDTO getOrg() {
        return org;
    }

    public void setOrg(NamedEntityDTO org) {
        this.org = org;
    }

    public EmploymentStatus getStatus() {
        return status;
    }

    public void setStatus(EmploymentStatus status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
