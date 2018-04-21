package com.zcurd.service;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.zcurd.common.DBTool;
import com.zcurd.common.DbMetaTool;
import com.zcurd.common.StringUtil;
import com.zcurd.common.ZcurdTool;
import com.zcurd.model.ZcurdField;
import com.zcurd.model.ZcurdHead;
import com.zcurd.vo.ZcurdMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 在线表单相关业务
 *
 * @author 钟世云 2016.2.5
 */
public class ZcurdService {

    private static final Logger logger = LoggerFactory.getLogger(ZcurdService.class);

    public void add(int headId, Map<String, String[]> paraMap) {

        ZcurdMeta mapmeta = getMetaData(headId);
        ZcurdHead head = mapmeta.getHead();
        List<ZcurdField> addFieldList = mapmeta.getAddFieldList();

        Record record = new Record();
        for (ZcurdField field : addFieldList) {
            String[] paramValues = paraMap.get("model." + field.getStr("field_name"));
            record.set(field.getStr("field_name"), paramValues == null ? null : paramValues[0]);
        }
        Db.use(ZcurdTool.getDbSource(head.getDbSource())).save(head.getStr("table_name"), head.getStr("id_field"), record);
    }

    public void update(int headId, int id, Map<String, String[]> paraMap) {
        ZcurdMeta mapmeta = getMetaData(headId);
        ZcurdHead head = mapmeta.getHead();
        List<ZcurdField> updateFieldList = mapmeta.getUpdateFieldList();

        Record record = get(headId, id);
        for (ZcurdField field : updateFieldList) {
            if (field.getInt("is_allow_update") == 1) {
                String[] paramValues = paraMap.get("model." + field.getStr("field_name"));
                record.set(field.getStr("field_name"), paramValues == null ? null : paramValues[0]);
            }
        }
        Db.use(ZcurdTool.getDbSource(head.getDbSource())).update(head.getStr("table_name"), head.getStr("id_field"), record);
    }

    public void delete(int headId, Integer[] ids) {
        ZcurdMeta mapmeta = getMetaData(headId);
        ZcurdHead head = mapmeta.getHead();

        for (Integer id : ids) {
            Db.use(ZcurdTool.getDbSource(head.getDbSource())).deleteById(head.getStr("table_name"), head.getStr("id_field"), id);
        }
        DbMetaTool.updateMetaData(headId);
    }

    /**
     * 求和计算
     *
     * @param mapmeta
     * @param properties
     * @param symbols
     * @param values
     * @return
     */
    public List<Map<String, Object>> getFooter(ZcurdMeta mapmeta, String[] properties, String[] symbols, Object[] values) {
        List<Map<String, Object>> footer = new ArrayList<>();
        ZcurdHead head = mapmeta.getHead();
        List<ZcurdField> footFieldList = mapmeta.getFooterFieldList();


        int footerSize = footFieldList.size();
        if (footerSize > 0) {
            StringBuilder sql = new StringBuilder("select ");
            for (int i = 0; i < footFieldList.size(); i++) {
                ZcurdField field = footFieldList.get(i);
                if (i > 0) {
                    sql.append(",");
                }
                sql.append(" sum(" + field.getStr("field_name") + ")");
            }
            sql.append(" from " + head.getTableName());

            //计算求和
            List<Object> list = DBTool.findDbSource(head.getDbSource(), sql.toString(), properties, symbols, values);
            if (null != list && list.size() > 0) {
                // 求和一列不是数组,需要处理
                Object[] result = footerSize > 1 ? (Object[]) list.get(0) : new Object[]{list.get(0)};
                Map<String, Object> sumMap = new HashMap<String, Object>();
                for (int i = 0; i < footFieldList.size(); i++) {
                    sumMap.put(footFieldList.get(i).getStr("field_name"), "<span style='color:blue;'>合计：" + result[i] + "</span>");
                }
                footer.add(sumMap);
            }
        }
        return footer;
    }


    public Record get(int headId, int id) {
        ZcurdMeta mapmeta = getMetaData(headId);
        ZcurdHead head = mapmeta.getHead();

        Record record = Db.use(ZcurdTool.getDbSource(head.getDbSource())).findById(head.getStr("table_name"), head.getStr("id_field"), id);
        return record;
    }

    public ZcurdMeta getMetaData(int headId) {
        return DbMetaTool.getMetaData(headId);
    }

    public ZcurdHead getHead(int headId) {
        return ZcurdHead.me.findById(headId);
    }

    public void genTableForm(final String tableName, String dbSource) {
        final String dbs = ZcurdTool.getDbSource(dbSource);
        Db.use(dbs).execute(new ICallback() {
            @Override
            public Object call(Connection conn) throws SQLException {
                DatabaseMetaData metaData = conn.getMetaData();
                String dbName = conn.getCatalog();

                ResultSet pkRSet = metaData.getPrimaryKeys(dbName, null, tableName);
                while (pkRSet.next()) {

                    //获得表注释（jdbc无法获取到)
                    String form_name = null;
                    try {
                        String sql = "select TABLE_COMMENT from information_schema.TABLES a where a.TABLE_SCHEMA=? and a.table_name=?";
                        form_name = Db.queryStr(sql, new Object[]{dbName, tableName});

                        if (StringUtil.isEmpty(form_name)) {
                            form_name = pkRSet.getString(3);
                        }

                        ZcurdHead head = new ZcurdHead()
                                .set("table_name", pkRSet.getObject(3))
                                .set("form_name", form_name)
                                .set("id_field", pkRSet.getObject(4))
                                .set("db_source", dbs);
                        if (!head.save()) {
                            logger.info("[生成表单][head数据保存失败]");
                        } else {
                            logger.info("[生成表单][head数据保存ok]");
                        }

                        logger.info("[生成表单][head保存成功][{}]", tableName);

                        genColumnInfo(metaData, dbName, tableName, head);

                    } catch (Exception e) {
                        logger.info("获得表注释失败！" + e.getMessage(), e);
                    }
                }
                return null;
            }
        });
    }

    /**
     * 生成表和视图数据配置信息
     *
     * @param tableName
     * @param dbSource
     * @param type
     */
    public void genForm(final String tableName, String dbSource, final String type) {
        if (StringUtil.equalsIgnoreCase(type, "VIEW")) {
            genViewForm(tableName, dbSource);
        } else {
            genTableForm(tableName, dbSource);
        }
    }


    /**
     * 视图生成表单
     *
     * @param viewName
     * @param dbSource
     */
    public void genViewForm(final String viewName, String dbSource) {
        final String dbs = ZcurdTool.getDbSource(dbSource);
        Db.use(dbs).execute(new ICallback() {
            @Override
            public Object call(Connection conn) throws SQLException {
                DatabaseMetaData metaData = conn.getMetaData();
                String dbName = conn.getCatalog();

                try {
                    ZcurdHead head = new ZcurdHead()
                            .set("table_name", viewName)
                            .set("form_name", "myview")
                            .set("id_field", "")
                            .set("db_source", dbs);
                    if (!head.save()) {
                        logger.error("[生成表单][head数据保存失败]");
                    } else {
                        logger.info("[生成表单][head数据保存ok]");
                    }

                    logger.info("[生成表单][head保存成功][{}]", viewName);

                    // 获取视图配置信息
                    genColumnInfo(metaData, dbName, viewName, head);

                } catch (Exception e) {
                    logger.info("获得视图注释失败！" + e.getMessage(), e);
                }
                return null;
            }
        });
    }

    /**
     * 生成列的配置信息
     *
     * @param metaData
     * @param dbName
     * @param tableName
     * @param head
     */
    private void genColumnInfo(DatabaseMetaData metaData, String dbName, String tableName, ZcurdHead head) {
        try {

            ResultSet colRet = metaData.getColumns(dbName, "%", tableName, "%");
            int orderNum = 2;
            while (colRet.next()) {
                String field_name = colRet.getString("COLUMN_NAME");
                String column_name = colRet.getString("REMARKS");
                if (StringUtil.isEmpty(column_name)) {
                    column_name = field_name;
                }

                ZcurdField field = new ZcurdField()
                        .set("head_id", head.getLong("id").intValue())
                        .set("field_name", field_name)
                        .set("column_name", column_name)
                        .set("data_type", colRet.getString("TYPE_NAME").toLowerCase())
                        .set("order_num", orderNum)
                        .set("is_allow_null", colRet.getInt("NULLABLE"));
                orderNum++;

                //主键
                if (field_name.equals(head.getIdField())) {
                    field.set("order_num", 1);
                    orderNum--;
                }

                //控件类型
                String dataType = field.getStr("data_type");
                String inputType = "easyui-textbox";
                if (dataType.equals("timestamp") || dataType.equals("date") || dataType.equals("datetime")) {
                    inputType = "easyui-datebox";
                } else if (dataType.equals("text")) {
                    inputType = "textarea";
                } else if (dataType.endsWith("int") || dataType.equals("long")) {
                    inputType = "easyui-numberspinner";
                }
                field.set("input_type", inputType);
                if (!field.save()) {
                    logger.error("[生成表单][field={}数据保存失败]", field_name);
                } else {
                    logger.info("[生成表单][field={}数据保存ok]", field_name);
                }
                logger.info("====================================field[{}]保存成功", field_name);
            }
        } catch (Exception e) {
            logger.info("获得列注释失败！" + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://101.200.193.169:3306/zcurd_base?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull", "root", "aikan");
        ActiveRecordPlugin arp = new ActiveRecordPlugin("zcurd", c3p0Plugin);
        c3p0Plugin.start();
        arp.start();

        final String tableName = "blog";
        Db.use("zcurd").execute(new ICallback() {
            @Override
            public Object call(Connection conn) throws SQLException {
                DatabaseMetaData metaData = conn.getMetaData();
                logger.info("数据库：" + conn.getCatalog());

                //获取tableName表列信息
                ResultSet tableSet = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
                while (tableSet.next()) {
                    logger.info(tableSet.getString("TABLE_NAME") + "	" + tableSet.getString("REMARKS"));
                }

                ResultSet colRet = metaData.getColumns(null, "%", tableName, "%");
                while (colRet.next()) {
                    String columnName = colRet.getString("COLUMN_NAME");
                    String columnType = colRet.getString("TYPE_NAME");
                    int datasize = colRet.getInt("COLUMN_SIZE");
                    int digits = colRet.getInt("DECIMAL_DIGITS");
                    int nullable = colRet.getInt("NULLABLE");
                    logger.info("字段：" + columnName + "\t" + columnType + "\t" + datasize + "\t" + digits + "\t" + nullable + "\t" + colRet.getString("REMARKS"));
                }

                ResultSet pkRSet = metaData.getPrimaryKeys(null, null, tableName);
                while (pkRSet.next()) {
                    System.err.println("****** Comment ******");
                    System.err.println("TABLE_CAT : " + pkRSet.getObject(1));
                    System.err.println("TABLE_SCHEM: " + pkRSet.getObject(2));
                    System.err.println("TABLE_NAME : " + pkRSet.getObject(3));
                    System.err.println("COLUMN_NAME: " + pkRSet.getObject(4));
                    System.err.println("KEY_SEQ : " + pkRSet.getObject(5));
                    System.err.println("PK_NAME : " + pkRSet.getObject(6));
                    System.err.println("****** ******* ******");
                }

                return null;
            }
        });
    }

}
