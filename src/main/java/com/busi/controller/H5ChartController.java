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
public class H5ChartController extends BaseController {


    public void list() {
        render("h5.html");
    }


    /**
     * 首页展示数据专用
     */
    public void chartdata() {

        Map<String, List<Object>> dataMap = new HashMap<>();
        List<Line> newcntList = new ArrayList<>();
        List<Line> newamtList = new ArrayList<>();
        Set<String> dateList = new TreeSet<>();
        List<Line> oldcntList = new ArrayList<>();
        List<Line> oldamtList = new ArrayList<>();
        Map<String, Line> newcntMap = new HashMap<>();
        Map<String, Line> oldcntMap = new HashMap<>();
        Map<String, Line> newamtMap = new HashMap<>();
        Map<String, Line> oldamtMap = new HashMap<>();

        // 获取新用户数据
        String sql = "SELECT new_cz_cnt, old_cz_cnt, new_cz_amt, old_cz_amt, DATE_FORMAT(date_time, '%Y-%m-%d') AS days, DATE_FORMAT(date_time, '%H') AS hours FROM h5_pay_stat_hour WHERE date_time >= '" + DateUtil.getBefore2Date(new Date()) + "' ORDER BY days ASC, hours ASC";
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
