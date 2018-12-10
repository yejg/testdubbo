package com.yejg.testdubbo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yejg.testdubbo.service.DubboInvokeService;
import com.yejg.testdubbo.model.PackageInfo;
import com.yejg.testdubbo.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboAction {

    private static Logger logger = LoggerFactory.getLogger(DubboAction.class);
    @Autowired
    private DubboInvokeService dubboInvokeService;

    @RequestMapping("/allinterface")
    public List<PackageInfo> queryInterface() {
        return dubboInvokeService.getPackageInfoList();
    }

    @RequestMapping("/invokeservice")
    public Map<String, Object> queryInterface(String myinterface, String mymethod, String params) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(myinterface) || StringUtils.isBlank(mymethod)) {
                throw new Exception("调用接口、调用方法不能为空");
            }
            myinterface = myinterface.trim();
            mymethod = mymethod.trim();
            List<String> dubboparams = new ArrayList<String>();
            if (!StringUtils.isBlank(params)) {
                try {
                    params = params.trim();
                    dubboparams = JsonUtil.toObject(params, List.class);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    throw new Exception("调用的参数格式错误，需要为标准的json数组");
                }
            }
            result.put("result", dubboInvokeService.invokeDubbo(myinterface, mymethod, dubboparams));
            result.put("error_no", "0");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("error_no", "-1");
            result.put("error_info", e.getMessage());
        }
        return result;
    }
}
