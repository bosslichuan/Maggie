package com.zcurd.common;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import org.beetl.core.GroupTemplate;
import org.beetl.ext.web.WebRender;

/**
 * @author Bossli
 * @date 2018/4/14
 */
public class BeetlRender extends Render {

    GroupTemplate gt = null;
    private transient static final String encoding = getEncoding();
    private transient static final String contentType = "text/html; charset=" + encoding;

    public BeetlRender(GroupTemplate gt, String view) {
        this.gt = gt;
        this.view = view;
    }

    @Override
    public void render() {
        try {
            response.setContentType(contentType);
            WebRender webRender = new WebRender(gt);
            webRender.render(view, request, response);

        } catch (Exception e) {
            throw new RenderException(e);
        }
    }
}
