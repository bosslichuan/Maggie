package com.busi.controller;

import com.busi.util.ColumnsUtils;
import com.jfinal.plugin.activerecord.Record;
import com.tools.EMailUtil;
import com.zcurd.DB;
import com.zcurd.common.DBTool;
import com.zcurd.common.StringUtil;
import com.zcurd.common.util.DateUtil;
import com.zcurd.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bossli on 2017/5/19.
 */
public class H5ComplainMailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(H5ComplainMailController.class);


    /**
     * H5邮件通知专用
     */
    public void mail() {

        String yesDate = DateUtil.get2DaysAgoYmd();
        String title = "【H5用户投诉日报】- " + yesDate;
        String to = getPara("to");
        List<Object> list = null;
        if (!StringUtil.isEmpty(to)) {
            list = new ArrayList<>();
            list.add(to);
        } else {
            list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_notify_h5_complain_email` ", new String[]{}, new String[]{}, new String[]{});
        }
        EMailUtil.sendMail(list, title, genHtml(yesDate, null));
        renderSuccess();
    }


    /**
     * 获取数据库中的数据
     *
     * @param yesDate
     * @param nowDate
     * @return
     */
    private List<Record> query(String yesDate, String nowDate) {
        // 小时数据
        String sql = "select date_format(date_time, '%Y-%m-%d %T') as date_time,host,uid,book_id,book_name,report_reason,report_content,tel from c_report_stat_total WHERE date_format(date_time, '%Y-%m-%d') = '" + yesDate + "'";
        List<Record> records = DBTool.use(DB.BUSI_208).find(sql);
        return records;
    }

    /**
     * 生成html数据
     *
     * @param yesDate
     * @param nowDate
     * @return
     */
    private String genHtml(String yesDate, String nowDate) {

        List<Record> newRecords = query(yesDate, nowDate);

        String str = null;
        try {
            str = ColumnsUtils.loadHtml("/zcurd/template/h5_complain.html", newRecords);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return str;
    }

}
