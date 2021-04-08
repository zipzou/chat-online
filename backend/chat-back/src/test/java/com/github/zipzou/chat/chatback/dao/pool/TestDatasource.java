package com.github.zipzou.chat.chatback.dao.pool;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * TestDatasource
 */
@SpringBootTest
public class TestDatasource {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private DataSourceProperties dataSourceProperties;

  @Test
  public void testDatasource() {
    assertNotNull(dataSourceProperties);
    System.out.println(dataSourceProperties);
    DataSource ds = applicationContext.getBean(DataSource.class);
    assertNotNull(ds);
  }
  
}