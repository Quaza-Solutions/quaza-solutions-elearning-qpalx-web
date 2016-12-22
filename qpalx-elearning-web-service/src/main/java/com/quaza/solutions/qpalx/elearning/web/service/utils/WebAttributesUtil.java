package com.quaza.solutions.qpalx.elearning.web.service.utils;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.service.WebAttributesUtil")
public class WebAttributesUtil implements IWebAttributesUtil {


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WebAttributesUtil.class);

    @Override
    public void addWebAttributes(Model model, Map<String, Object> webAttributes) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(webAttributes, "webAttributes cannot be null");

        LOGGER.debug("Adding all webAttributes:> {} to model....", webAttributes);
        webAttributes.forEach((k, v) -> {
            model.addAttribute(k, v);
        });
    }
}
