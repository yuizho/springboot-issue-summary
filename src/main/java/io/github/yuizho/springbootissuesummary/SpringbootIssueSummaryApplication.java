package io.github.yuizho.springbootissuesummary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootIssueSummaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootIssueSummaryApplication.class, args);
	}

}

