package com.mock.conloop.model;

import java.io.Serializable;

public class Role implements Serializable{
    private static final long serialVersionUID = 346775453236789876L;
    private String roleId;
    private String roleName;

    public Role(){}
    public Role(String roleId, String roleName){
        this.roleId = roleId;
        this.roleName = roleName;
    }
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
