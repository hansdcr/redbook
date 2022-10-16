package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "org.example.mapper")
// @ComponentScan(basePackages = {"org.example", "org.n3r.idworker"}) // 如果有包扫描不到，可以在这里加入
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
