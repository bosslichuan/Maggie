package com.zcurd.common;

import com.jfinal.kit.PathKit;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;

import java.io.IOException;

/**
 * @author Bossli
 * @date 2018/4/14
 */
public class Jfinal2BeetlRenderFactory implements IMainRenderFactory {
    public static String viewExtension = ".html";
    public static GroupTemplate groupTemplate = null;

    public Jfinal2BeetlRenderFactory() {
        init(PathKit.getWebRootPath());
        //        init(null); use jfinalkit instead

    }

    public Jfinal2BeetlRenderFactory(ResourceLoader resourceLoader) {
        if (groupTemplate != null) {
            groupTemplate.close();
        }
        try {

            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(resourceLoader, cfg);
        } catch (IOException e) {
            throw new RuntimeException("加载GroupTemplate失败", e);
        }
    }

    public Jfinal2BeetlRenderFactory(String templateRoot) {

        init(templateRoot);

    }

    private void init(String root) {
        if (groupTemplate != null) {
            groupTemplate.close();
        }

        try {

            Configuration cfg = Configuration.defaultConfiguration();
            WebAppResourceLoader resourceLoader = new WebAppResourceLoader(root);
            groupTemplate = new GroupTemplate(resourceLoader, cfg);

        } catch (IOException e) {
            throw new RuntimeException("加载GroupTemplate失败", e);
        }
    }

    @Override
    public Render getRender(String view) {
        return new BeetlRender(groupTemplate, view);
    }

    @Override
    public String getViewExtension() {
        return viewExtension;
    }
}
