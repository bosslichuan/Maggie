package com.busi.util;

import com.jfinal.plugin.activerecord.Record;
import com.zcurd.common.DBTool;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.WebAppResourceLoader;

import java.util.List;

/**
 * Created by Bossli on 2017/6/27.
 */
public class ColumnsUtils {


    static Configuration cfg = null;
    static GroupTemplate gt = null;

    static {
        try {
            cfg = Configuration.defaultConfiguration();
            gt = new GroupTemplate(new WebAppResourceLoader(), cfg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private ColumnsUtils() {
    }

    /**
     * 客户端反馈问题文本生成器
     *
     * @param sql
     * @param dataSource
     * @param cols
     * @param title
     * @return
     */
    public static String callbackTxt(String sql, String dataSource, String[] cols, String title) {
        List<Record> records = DBTool.use(dataSource).find(sql);
        StringBuilder sb = new StringBuilder(title).append("<br>");
        for (Record r : records) {
            String line = "";
            for (String col : cols) {
                Object cell = r.get(col);
                line += cell + "\t";
            }
            sb.append(line).append("<br>");
        }
        return sb.toString();
    }

    /**
     * 生成对比数据tr
     *
     * @param record
     * @param cols
     * @param index
     * @return
     */
    public static String subHtml(Record record, String[] cols, int index) {

        StringBuilder sb = new StringBuilder();

        // 行间隔上色
        if (24 == index) {
            sb.append("<tr style=\"background-color:#ddeeff\">");
        } else if (index % 2 == 0) {
            sb.append("<tr style=\"background-color:#f1f1f1\">");
        } else {
            sb.append("<tr>");
        }

        Number col1 = record.get(cols[0]);
        Number col2 = record.get(cols[1]);
        Number col3 = record.get(cols[2]);
        Number col4 = record.get(cols[3]);
        Number col5 = record.get(cols[4]);
        Number col6 = record.get(cols[5]);

        sb.append("<td style=\"padding:5px 0px;word-break: break-all; word-wrap:break-word;\">").append(record.get("hour")).append("</td>");
        sb.append("<td style=\"padding:5px 0px;word-break: break-all; word-wrap:break-word;\">").append(emptyData(col1)).append("</td>");
        sb.append(handlerDiff(col1, col2));
        sb.append("<td style=\"padding:5px 0px;word-break: break-all; word-wrap:break-word;\">").append(emptyData(col3)).append("</td>");
        sb.append(handlerDiff(col3, col4));
        sb.append("<td style=\"padding:5px 0px;word-break: break-all; word-wrap:break-word;\">").append(emptyData(col5)).append("</td>");
        sb.append(handlerDiff(col5, col6));

        sb.append("</tr>");
        return sb.toString();
    }

    /**
     * 隔行换色
     *
     * @param o1
     * @param o2
     * @return
     */
    private static String handlerDiff(Object o1, Object o2) {
        String ret = "";
        if (null == o1) {
            o1 = 0;
        }
        if (null == o2 || ((Number) 0).longValue() == ((Number) o2).longValue()) {
            ret = "<td>-</td>";
        } else {
            if (o1 instanceof Number) {
                if (((Number) o1).doubleValue() > ((Number) o2).doubleValue()) {
                    ret = "<td style=\"word-break: break-all; word-wrap:break-word;color:#03AA46; padding:5px\">" + o2.toString() + "</td>";
                } else {
                    ret = "<td style=\"word-break: break-all; word-wrap:break-word;color:#E9081E; padding:5px\">" + o2.toString() + "</td>";
                }
            }
        }
        return ret;
    }

    private static String emptyData(Number n) {
        return null == n ? "-" : String.valueOf(n);
    }

    public static String loadHtml(String templte, List<Record> list) throws Throwable {
        Template t = gt.getTemplate(templte);
        t.binding("list", list);
        return t.render();
    }
    public static String loadHtml(String templte, List<Record> list, List<Record> list2) throws Throwable {
        Template t = gt.getTemplate(templte);
        t.binding("list", list);
        t.binding("list2", list2);
        return t.render();
    }
}
