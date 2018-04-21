package com.zcurd.common;

import com.busi.controller.*;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.zcurd.DB;
import com.zcurd.common.handler.ZcurdHandler;
import com.zcurd.common.interceptor.AuthInterceptor;
import com.zcurd.controller.*;
import com.zcurd.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * API引导式配置
 */
public class ZcurdConfig extends JFinalConfig {
    private static final Logger logger = LoggerFactory.getLogger(ZcurdConfig.class);

    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("system.properties");
        me.setDevMode(PropKit.getBoolean("devMode", false));

        me.setMainRenderFactory(new Jfinal2BeetlRenderFactory());
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes me) {
        me.add("/login", LoginController.class, "/zcurd/login");
        me.add("/zcurd", ZcurdController.class, "/zcurd/zcurd");
        me.add("/zcurdHead", ZcurdHeadController.class, "/zcurd");
        me.add("/menu", MenuController.class, "/zcurd/menu");
        me.add("/main", MainController.class, "/zcurd");
        me.add("/role", RoleController.class, "/zcurd/role");
        me.add("/common", CommonController.class, "/zcurd");
        me.add("/oplog", SysOplogController.class, "/zcurd/sysOplog");
        me.add("/user", SysUserController.class, "/zcurd/sysUser");
//        me.add("/stockHistoryLog", StockHistoryLogController.class, "/busi/stockHistoryLog");
//        me.add("/clawBookUrl", ClawBookUrlController.class, "/busi/clawBookUrl");
        // H5分小时收入图表数据
        me.add("/h5/chart", H5ChartController.class, "/zcurd/charts/");
        // 客户端分小时收入图表数据
        me.add("/owch/chart", OwchChartController.class, "/zcurd/charts/");
        // H5分小时收入邮件接口,大数据调用
        me.add("/portal", H5MailController.class);
        // 自有客户端反馈邮件通知接口,本地调用
        me.add("/portal/cs", CustomServiceController.class);

        // 自有客户端全产品线分小时收入邮件接口,大数据调用
        me.add("/portal/owch", OwchMailController.class);
        // 基地客户端反馈邮件通知接口,本地调用
        me.add("/portal/base", BaseMailController.class);
        // 基地客户端反馈邮件通知接口,本地调用
        me.add("/portal/ios", IOSMailController.class);
        // H5分站小时报
        me.add("/portal/h5/domain", H5DomainMailController.class);
        // H5投诉邮件通知
        me.add("/portal/h5/complain", H5ComplainMailController.class);
        // H5CPS 分析小时邮件通知
        me.add("/portal/h5cps", H5CPSMailController.class);
    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
        C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("base.jdbcUrl"), PropKit.get("base.user"), PropKit.get("base.password").trim());
        me.add(c3p0Plugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(DB.BASTIC_208, c3p0Plugin);
        arp.setShowSql(true);
        me.add(arp);
        arp.addMapping("zcurd_head", ZcurdHead.class);
        arp.addMapping("zcurd_field", ZcurdField.class);
        arp.addMapping("zcurd_head_btn", ZcurdHeadBtn.class);
        arp.addMapping("zcurd_head_js", ZcurdHeadJs.class);
        arp.addMapping("sys_menu", Menu.class);
        arp.addMapping("sys_menu_btn", MenuBtn.class);
        arp.addMapping("sys_menu_datarule", MenuDatarule.class);
        arp.addMapping("sys_user", SysUser.class);
        arp.addMapping("sys_menu_btn", SysMenuBtn.class);
        arp.addMapping("sys_oplog", SysOplog.class);
        arp.addMapping("common_file", CommonFile.class);

        //业务数据库
        C3p0Plugin c3p0PluginAir = new C3p0Plugin(PropKit.get("busi.jdbcUrl"), PropKit.get("busi.user"), PropKit.get("busi.password").trim());
        me.add(c3p0PluginAir);
        ActiveRecordPlugin arpAir = new ActiveRecordPlugin(DB.BUSI_208, c3p0PluginAir);
        arpAir.setShowSql(true);
        me.add(arpAir);
        // 115ASG业务数据库
        C3p0Plugin c3p0PluginAsg = new C3p0Plugin(PropKit.get("asg.jdbcUrl"), PropKit.get("asg.user"), PropKit.get("asg.password").trim());
        me.add(c3p0PluginAsg);
        ActiveRecordPlugin arpAsg = new ActiveRecordPlugin(DB.ASG_115, c3p0PluginAsg);
        arpAsg.setShowSql(true);

//		arpAir.addMapping("stock_history_log", StockHistoryLog.class);
//		arpAir.addMapping("claw_book_url", ClawBookUrl.class);


        me.add(arpAsg);
    }

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new SessionInViewInterceptor());
        me.add(new AuthInterceptor());
        me.add(new ErrorInterceptor());
    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers me) {
        me.add(new ZcurdHandler());

    }

    @Override
    public void afterJFinalStart() {
        try {
//            FreeMarkerRender.getConfiguration().setSharedVariable("basePath", JFinal.me().getContextPath());
            HashMap<String, Object> map = new HashMap<>();
            map.put("basePath", JFinal.me().getContextPath());
            Jfinal2BeetlRenderFactory.groupTemplate.setSharedVars(map);
            logger.info("Initializing completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 建议使用 JFinal 手册推荐的方式启动项目
     * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        JFinal.start("webapp", 8080, "/", 5);
    }
}
