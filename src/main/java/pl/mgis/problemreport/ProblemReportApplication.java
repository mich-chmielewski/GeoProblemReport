package pl.mgis.problemreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class ProblemReportApplication {

    private static final Logger log = LoggerFactory.getLogger(ProblemReportApplication.class);

    public static void main(String[] args) throws IOException {

        log.info("Absolute/WorkingDir Path: " + new File("").getAbsolutePath());
        SpringApplication.run(ProblemReportApplication.class, args);


    }

}
