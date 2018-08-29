package com.apolloframework.scheduling;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Scheduler configuration for autowired quartz objects.
 * The quartz settings must be in a file called <code>quartz.properties</code>
 * located in the resources folder
 * @author amarenco
 *
 */
public abstract class SpringQuartzScheduler {
    @Autowired
    private ApplicationContext applicationContext;

    
    @Bean
    public abstract List<Trigger> quartzTriggers();
    

    @Bean
    public JobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
    
    

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, @Qualifier("quartzTriggers") List<Trigger> triggers)  throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setAutoStartup(true);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        
        if (triggers != null && !triggers.isEmpty()) {
            factory.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
        }

        return factory;
    }
}
