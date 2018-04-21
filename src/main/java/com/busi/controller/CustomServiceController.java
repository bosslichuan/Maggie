package com.busi.controller;

import com.busi.service.ClientCallbackService;
import com.busi.service.impl.ClientCallbackServiceImpl;
import com.zcurd.controller.BaseController;

/**
 * Created by Bossli on 2017/5/19.
 */
public class CustomServiceController extends BaseController {

    /**
     * 客服反馈邮件通知接口
     */
    public void uninstall() {

        ClientCallbackService clientCallbackService = new ClientCallbackServiceImpl();
        boolean ret = false;
        // 卸载反馈
        ret = clientCallbackService.uninstallCallback();
        if (ret) {
            renderSuccess();
        } else {
            renderFailed();
        }
    }
    /**
     * 客服反馈邮件通知接口
     */
    public void chapter() {

        ClientCallbackService clientCallbackService = new ClientCallbackServiceImpl();
        boolean ret = false;
        // 章节错乱反馈
        ret = clientCallbackService.chapterErrorCallback();

        if (ret) {
            renderSuccess();
        } else {
            renderFailed();
        }
    }

    /**
     * 客服反馈邮件通知接口
     */
    public void search() {

        ClientCallbackService clientCallbackService = new ClientCallbackServiceImpl();
        boolean ret = false;
        // 搜索反馈
        ret = clientCallbackService.searchCallback();

        if (ret) {
            renderSuccess();
        } else {
            renderFailed();
        }
    }

}
