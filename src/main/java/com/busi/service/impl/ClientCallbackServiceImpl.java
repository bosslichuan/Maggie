package com.busi.service.impl;

import com.busi.service.ClientCallbackService;
import com.busi.util.ColumnsUtils;
import com.tools.EMailUtil;
import com.zcurd.DB;
import com.zcurd.common.DBTool;
import com.zcurd.common.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Bossli on 2017/6/27.
 */
public class ClientCallbackServiceImpl implements ClientCallbackService {

    /**
     * 客户端卸载反馈邮件
     * 过滤指定测试用户
     *
     * @return
     */
    @Override
    public boolean uninstallCallback() {
        String date = DateUtil.getBeforeDate(new Date());
        String sql = "SELECT\n" +
                "\tDATE_FORMAT(record_date, '%Y-%m-%d') as record_date,\n" +
                "\tuser_id,\n" +
                "\timsi,\n" +
                "\tuser_tel,\n" +
                "\tversion,\n" +
                "\tcallback_content\n" +
                "FROM\n" +
                "\t`b_callback`\n" +
                "WHERE user_id!=98796854 and user_id != 75606702 and imsi != '460001221852828' and \n" +
                "\trecord_date = '" + date + "'";

        String tstr = "日期\t用户id\timsi\t手机号\t版本\t内容";
        String txt = ColumnsUtils.callbackTxt(sql, DB.ASG_115, new String[]{"record_date", "user_id", "imsi", "user_tel", "version", "callback_content"}, tstr);
        String title = date + "客户端卸载问题反馈";
        List<Object> list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_client_uninstall_email` ", new String[]{}, new String[]{}, new String[]{});
        return EMailUtil.sendMail(list, title, txt);
    }

    /**
     * 客户端章节反馈邮件
     * 过滤指定测试用户
     *
     * @return
     */
    @Override
    public boolean chapterErrorCallback() {
        String date = DateUtil.getBeforeDate(new Date());
        String startTime = date + " 00:00:00";
        String endTime = date + " 23:59:59";
        String sql = "SELECT\n" +
                "\tDATE_FORMAT(create_time, '%Y-%m-%d') AS record_date,\n" +
                "\tuser_id,\n" +
                "\tbook_id,\n" +
                "\tchapter_id,\n" +
                "\tchapter_name,\n" +
                "\timsi,\n" +
                "\tuser_tel,\n" +
                "\tversion,\n" +
                "\terror_code,\n" +
                "\terror_desc\n" +
                "FROM\n" +
                "\t`b_chapter_disorder`\n" +
                "WHERE user_id!=98796854 and user_id != 75606702 and imsi != '460001221852828' and \n" +
                "\tcreate_time >= '" + startTime + "'\n" +
                "AND create_time <= '" + endTime + "'";

        String tstr = "日期\t用户id\t书籍id\t章节id\t章节名称\timsi\t手机号\t版本\t错误描述";
        String[] cols = new String[]{"record_date", "user_id", "book_id", "chapter_id", "chapter_name", "imsi", "user_tel", "version", "error_desc"};
        String txt = ColumnsUtils.callbackTxt(sql, DB.ASG_115, cols, tstr);
        String title = date + "章节问题反馈";
        List<Object> list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_client_chapter_error_email` ", new String[]{}, new String[]{}, new String[]{});
        return EMailUtil.sendMail(list, title, txt);
    }

    @Override
    public boolean searchCallback() {
        String date = DateUtil.getBeforeDate(new Date());
        String startTime = date + " 00:00:00";
        String endTime = date + " 23:59:59";
        String sql = "SELECT\n" +
                "\tuser_id,\n" +
                "\tchannels,\n" +
                "\tCASE\n" +
                "WHEN user_type = 1 THEN\n" +
                "\t'基地'\n" +
                "ELSE\n" +
                "\t'自有'\n" +
                "END AS user_type,\n" +
                "versions,\n" +
                " callback_content,\n" +
                " improvement\n" +
                "FROM\n" +
                "\t`b_feedback_search`\n" +
                "WHERE\n" +
                "\tctime >= '" + startTime + "'\n" +
                "AND ctime <= '" + endTime + "'";

        String tstr = "用户id\t渠道号\t用户类型\t版本号\t反馈内容\t改进意见";
        String[] cols = new String[]{"user_id", "channels", "user_type", "versions", "callback_content", "improvement"};
        String txt = ColumnsUtils.callbackTxt(sql, DB.ASG_115, cols, tstr);
        String title = date + "搜索反馈";
        List<Object> list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_search_feedback_email`", new String[]{}, new String[]{}, new String[]{});
        return EMailUtil.sendMail(list, title, txt);
    }
}
