package com.nalyvaiko;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  private static Logger logger = LogManager.getLogger();

  public static void main(String[] args) {
    logger.info("Application start");
    SpringApplication.run(Application.class, args);
  }
}
