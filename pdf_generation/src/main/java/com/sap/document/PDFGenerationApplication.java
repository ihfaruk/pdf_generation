package com.sap.document;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = { "com.sap" })
public class PDFGenerationApplication {

	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(PDFGenerationApplication.class);
		sa.setLogStartupInfo(false);
		sa.setBannerMode(Banner.Mode.OFF);
		sa.run(args);

	}

}
