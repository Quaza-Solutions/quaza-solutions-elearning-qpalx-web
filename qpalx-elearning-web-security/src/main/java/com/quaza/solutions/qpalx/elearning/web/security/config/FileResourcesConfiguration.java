package com.quaza.solutions.qpalx.elearning.web.security.config;

import com.quaza.solutions.qpalx.elearning.domain.sstatic.content.StaticContentConfiguration;
import com.quaza.solutions.qpalx.elearning.domain.sstatic.content.StaticContentConfigurationTypeE;
import com.quaza.solutions.qpalx.elearning.service.sstatic.content.IStaticContentConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Required configuration to make external file system directories available for use in the application.
 *
 * Using strategies defined in: http://blog.kaliatech.com/2014/11/adding-external-directory-to-spring-boots-static-resources-handling/
 *
 * @author manyce400
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class FileResourcesConfiguration extends WebMvcConfigurerAdapter {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StaticContentConfigurationService")
    private IStaticContentConfigurationService iStaticContentConfigurationService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FileResourcesConfiguration.class);



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        StaticContentConfiguration eLearninStaticContentConfiguration = iStaticContentConfigurationService.findStaticContentConfigurationByContentName(StaticContentConfigurationTypeE.ELearningContent);
        String eLearningResourceHandler = eLearninStaticContentConfiguration.getStaticContentApplicationContextLocation() + "**";
        String eLearningResourcePhysicalDir = "file://" +  eLearninStaticContentConfiguration.getStaticContentPhysicalLocation();
        LOGGER.info("Mapping Elearning resourceHandler: {} to directory:> {}", eLearningResourceHandler, eLearningResourcePhysicalDir);
        registry.addResourceHandler(eLearningResourceHandler)
                .addResourceLocations(eLearningResourcePhysicalDir);
        super.addResourceHandlers(registry);
    }
}
