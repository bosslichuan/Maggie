package com.busi.controller;

import com.busi.util.ColumnsUtils;
import com.jfinal.plugin.activerecord.Record;
import com.tools.EMailUtil;
import com.zcurd.DB;
import com.zcurd.common.DBTool;
import com.zcurd.common.StringUtil;
import com.zcurd.common.util.DateUtil;
import com.zcurd.controller.BaseController;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.WebAppResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Bossli on 2017/5/19.
 */
public class IOSMailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(IOSMailController.class);


    /**
     * IOS客户端邮件通知专用
     */
    public void mail() {

        String yesDate = DateUtil.get2DaysAgoYmd();
        String nowDate = DateUtil.getOneHourAgoYmd();
        String title = "iOS客户端分小时新老数据" + yesDate + "、" + nowDate + "对比";
        String to = getPara("to");
        List<Object> list = null;
        if (!StringUtil.isEmpty(to)) {
            list = new ArrayList<>();
            list.add(to);
        } else {
            list = DBTool.findDbSource("zcurd_busi", "select `email` from `b_ios_notify_email` ", new String[]{}, new String[]{}, new String[]{});
        }
        EMailUtil.sendMail(list, title, genHtml(yesDate, nowDate));
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
        String sql = "SELECT date_format(a.date_time, '%H') AS `hour`, a.v_cnt AS v_cnt1, a.new_v_cnt AS new_v_cnt1, a.old_v_cnt AS old_v_cnt1," +
                " a.cz_cnt AS cz_cnt1, a.new_cz_cnt AS new_cz_cnt1, a.old_cz_cnt AS old_cz_cnt1, a.cz_amt AS cz_amt1, a.new_cz_amt AS new_cz_amt1," +
                " a.old_cz_amt AS old_cz_amt1, a.xf_cnt AS xf_cnt1, a.new_xf_cnt AS new_xf_cnt1, a.old_xf_cnt AS old_xf_cnt1, a.xf_amt AS xf_amt1," +
                " a.new_xf_amt AS new_xf_amt1, a.old_xf_amt AS old_xf_amt1, a.xf_award AS xf_award1, a.new_xf_award AS new_xf_award1," +
                " a.old_xf_award AS old_xf_award1, b.v_cnt AS v_cnt2, b.new_v_cnt AS new_v_cnt2, b.old_v_cnt AS old_v_cnt2, b.cz_cnt AS cz_cnt2," +
                " b.new_cz_cnt AS new_cz_cnt2, b.old_cz_cnt AS old_cz_cnt2, b.cz_amt AS cz_amt2, b.new_cz_amt AS new_cz_amt2, b.old_cz_amt AS old_cz_amt2," +
                " b.xf_cnt AS xf_cnt2, b.new_xf_cnt AS new_xf_cnt2, b.old_xf_cnt AS old_xf_cnt2, b.xf_amt AS xf_amt2, b.new_xf_amt AS new_xf_amt2," +
                " b.old_xf_amt AS old_xf_amt2, b.xf_award AS xf_award2, b.new_xf_award AS new_xf_award2, b.old_xf_award AS old_xf_award2 " +
                "FROM i_order_consume_hour a LEFT JOIN i_order_consume_hour b ON date_format(a.date_time, '%H') = date_format(b.date_time, '%H') " +
                "AND date_format(b.date_time, '%Y-%m-%d') = '" + nowDate + "' WHERE date_format(a.date_time, '%Y-%m-%d') = '" + yesDate + "' ORDER BY `hour` ASC";
        List<Record> records = DBTool.use(DB.BUSI_208).find(sql);

        // 汇总数据
        String sqlCalc = "SELECT '汇总' AS `hour`, a.v_cnt AS v_cnt1, a.new_v_cnt AS new_v_cnt1, a.old_v_cnt AS old_v_cnt1, a.cz_cnt AS cz_cnt1," +
                " a.new_cz_cnt AS new_cz_cnt1, a.old_cz_cnt AS old_cz_cnt1, a.cz_amt AS cz_amt1, a.new_cz_amt AS new_cz_amt1, a.old_cz_amt AS old_cz_amt1," +
                " a.xf_cnt AS xf_cnt1, a.new_xf_cnt AS new_xf_cnt1, a.old_xf_cnt AS old_xf_cnt1, a.xf_amt AS xf_amt1, a.new_xf_amt AS new_xf_amt1," +
                " a.old_xf_amt AS old_xf_amt1, a.xf_award AS xf_award1, a.new_xf_award AS new_xf_award1, a.old_xf_award AS old_xf_award1, b.v_cnt AS v_cnt2," +
                " b.new_v_cnt AS new_v_cnt2, b.old_v_cnt AS old_v_cnt2, b.cz_cnt AS cz_cnt2, b.new_cz_cnt AS new_cz_cnt2, b.old_cz_cnt AS old_cz_cnt2," +
                " b.cz_amt AS cz_amt2, b.new_cz_amt AS new_cz_amt2, b.old_cz_amt AS old_cz_amt2, b.xf_cnt AS xf_cnt2, b.new_xf_cnt AS new_xf_cnt2," +
                " b.old_xf_cnt AS old_xf_cnt2, b.xf_amt AS xf_amt2, b.new_xf_amt AS new_xf_amt2, b.old_xf_amt AS old_xf_amt2, b.xf_award AS xf_award2," +
                " b.new_xf_award AS new_xf_award2, b.old_xf_award AS old_xf_award2 FROM i_order_consume_lj_hour a LEFT JOIN i_order_consume_lj_hour b " +
                "ON date_format(a.date_time, '%H') = date_format(b.date_time, '%H') AND date_format(b.date_time, '%Y-%m-%d') = '" + nowDate + "' " +
                "WHERE date_format(a.date_time, '%Y-%m-%d') = '" + yesDate + "' ORDER BY `hour` ASC";
        records.addAll(DBTool.use(DB.BUSI_208).find(sqlCalc));
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
            str = ColumnsUtils.loadHtml("/zcurd/template/ios.html", newRecords);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        return str;
    }


    public void test() throws IOException {
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(new WebAppResourceLoader(), cfg);
        Template t = gt.getTemplate("/zcurd/template/mail.html");
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        t.binding("list", list);
        System.out.println(t.render());
        renderSuccess();
    }

}
