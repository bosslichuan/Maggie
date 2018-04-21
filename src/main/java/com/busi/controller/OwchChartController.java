package com.busi.controller;

import com.busi.model.Line;
import com.jfinal.plugin.activerecord.Record;
import com.zcurd.DB;
import com.zcurd.common.DBTool;
import com.zcurd.common.util.DateUtil;
import com.zcurd.controller.BaseController;

import java.util.*;

/**
 * Created by Bossli on 2017/5/19.
 */
public class OwchChartController extends BaseController {

    public void list() {
        render("owch.html");
    }

    /**
     * 图表数据生成
     */
    public void chartdata() {

        Map<String, List<Object>> dataMap = new HashMap<>();
        Set<String> dateList = new TreeSet<>();
        Map<String, Line> newcntMap = new HashMap<>();
        Map<String, Line> oldcntMap = new HashMap<>();
        Map<String, Line> newamtMap = new HashMap<>();
        Map<String, Line> oldamtMap = new HashMap<>();

        // 获取新用户数据
        String sql = "SELECT\n" +
                "\tDATE_FORMAT(date_time, '%H') AS hours,\n" +
                "\tDATE_FORMAT(date_time, '%Y-%m-%d') AS days,\n" +
                "\tsum(new_cz_cnt) AS new_cz_cnt,\n" +
                "\tsum(old_cz_cnt) AS old_cz_cnt,\n" +
                "\tsum(new_cz_amt) AS new_cz_amt,\n" +
                "\tsum(old_cz_amt) AS old_cz_amt\n" +
                "FROM\n" +
                "\tc_order_consume_hour\n" +
                "WHERE\n" +
                "\tdate_time >= '" + DateUtil.getBefore2Date(new Date()) + "'\n" +
                "GROUP BY\n" +
                "\tdays,\n" +
                "\thours\n" +
                "ORDER BY\n" +
                "\tdays ASC,\n" +
                "\thours ASC";
        List<Record> newRecords = DBTool.use(DB.BUSI_208).find(sql);
        for (Record record : newRecords) {
            String date = record.get("days");
            dateList.add(date);
            sub(newcntMap, date, record.get("new_cz_cnt"));
            sub(oldcntMap, date, record.get("old_cz_cnt"));
            sub(newamtMap, date, record.get("new_cz_amt"));
            sub(oldamtMap, date, record.get("old_cz_amt"));
        }

        dataMap.put("newcnt", new ArrayList<Object>(newcntMap.values()));
        dataMap.put("newamt", new ArrayList<Object>(newamtMap.values()));
        dataMap.put("oldcnt", new ArrayList<Object>(oldcntMap.values()));
        dataMap.put("oldamt", new ArrayList<Object>(oldamtMap.values()));
        dataMap.put("dateList", new ArrayList<Object>(dateList));

        renderMapEcharts(dataMap);
    }

    /**
     * 处理数据为echart格式对象
     * @param map
     * @param key
     * @param value
     */
    private void sub(Map<String, Line> map, String key, Object value) {
        if (null == map.get(key)) {
            Line line = new Line();
            line.setName(key);
            line.setType("line");
            line.setSymbol("none");
            map.put(key, line);
        }
        if (null == map.get(key).getData()) {
            map.get(key).setData(new ArrayList<Object>());
        }
        map.get(key).getData().add(value);

    }
}
