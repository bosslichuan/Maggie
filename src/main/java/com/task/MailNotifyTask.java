package com.task;

import com.tools.EMailUtil;
import com.zcurd.common.DBTool;

import java.util.List;

/**
 * Created by Bossli on 2017/6/9.
 */
public class MailNotifyTask {

    public void sendMail(){
//        List<Object> list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_notify_email` ", new String[]{}, new String[]{}, new String[]{});
//        EMailUtil.sendMail(list,EMailUtil.genHtml());
    }
}
