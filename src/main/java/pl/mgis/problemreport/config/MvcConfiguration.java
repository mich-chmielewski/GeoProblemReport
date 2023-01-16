package pl.mgis.problemreport.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.mgis.problemreport.ProblemReportApplication;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Set;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    public static final String FILES_DIR = "./app_data/files/";
    public static final String FILES_URL = "/files/";
    public static final String DATA_DIR = "./app_data/dataset/";

    private Set<HandlerInterceptor> handlerInterceptors;

    public MvcConfiguration(Set<HandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors = handlerInterceptors;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        *OneByOneAdding
        */
        //registry.addInterceptor(new RequestLoggerInterceptor());

        /*
         *Auto  OneByOneAdding
         */
        handlerInterceptors.forEach(registry::addInterceptor);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            Files.createDirectories(Paths.get(FILES_DIR));
            Files.createDirectories(Paths.get(DATA_DIR));
            registry
                    .addResourceHandler(FILES_URL + "**")
                    .addResourceLocations("file:" + FILES_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}