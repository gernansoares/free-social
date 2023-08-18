package com.freesocial.lib.properties;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ResourceBundle;

/**
 * Maps errors.properties as a bean and can be used to read its values
 */
@Configuration
public class ErroUtil {

    private static final String FILE = "errors";

    private static ResourceBundle resourceBundle;

    @Bean
    public MessageSource errosSource() {
        ReloadableResourceBundleMessageSource errosSource = new ReloadableResourceBundleMessageSource();
        errosSource.setBasename("classpath:" + FILE);
        return errosSource;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(errosSource());
        return bean;
    }

    static {
        resourceBundle = ResourceBundle.getBundle(FILE);
    }

    /**
     * Reads a property value without parameters
     *
     * @param message the property to be read
     * @return the message associated with the property
     */
    public static String getMessage(String message) {
        String msg = resourceBundle.getString(message);
        return msg.format(msg);
    }

    /**
     * Reads a property value with parameters
     *
     * @param message the property to be read
     * @param parameters the parameters which will be replaced in the message, in order
     * @return the message associated with the property with all parameters replaced
     */
    public static String getMessage(String message, String... parameters) {
        String msg = resourceBundle.getString(message);
        return msg.format(msg, parameters);
    }

}
