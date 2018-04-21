package com.busi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Bossli on 2017/6/8.
 */
public class service {

    private static final Logger logger = LoggerFactory.getLogger(service.class);

    public void mailNotify(){
        logger.info("[邮件通知任务][开始]");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("lichuan@ishugui.com");
//        EMailUtil.sendMail(arrayList,"哈哈哈");
        logger.info("[邮件通知任务][结束]");
    }

}
