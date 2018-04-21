package com.zcurd.model;

import com.jfinal.plugin.activerecord.Model;
import com.zcurd.common.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MenuBtn extends Model<MenuBtn> {
    private static final long serialVersionUID = 1L;
    public static final MenuBtn me = new MenuBtn();

    public List<MenuBtn> findAll() {
        return find("select * from sys_menu_btn");
    }

    public List<MenuBtn> findByUser(SysUser user) {
        List<MenuBtn> result = new ArrayList<MenuBtn>();
        String roles = user.getStr("roles");
        if (StringUtil.isNotEmpty(roles)) {
            result = find("select distinct b.* from sys_role_btn a join sys_menu_btn b on a.btn_id=b.id where a.role_id in(" + roles + ")");
        }
        return result;
    }

    public List<MenuBtn> findUserNoAuthBtns(SysUser user) {
        List<MenuBtn> result = new ArrayList<MenuBtn>();
        String roles = user.getStr("roles");
        if (StringUtil.isNotEmpty(roles)) {
            result = find("select * from sys_menu_btn a WHERE a.id not in(select btn_id from sys_role_btn where role_id in(" + roles + "))");
        }
        return result;
    }

}
