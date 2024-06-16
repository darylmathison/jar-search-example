package org.mathison.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class TestJarsSearch {

  public static final String ROOT_PATH = "./target/test-classes";
  public static final File TEST2_JAR_FILE = new File(ROOT_PATH, "jars/subJars/test2.jar");
  public static final File TEST_JAR_FILE = new File(ROOT_PATH, "jars/alltest.jar");

  @Test
  public void testFind() throws IOException {
    assertTrue(TEST2_JAR_FILE.exists());
    assertTrue(TEST_JAR_FILE.exists());

    JarsSearch js = new JarsSearch();
    js.setRootpath(ROOT_PATH);
    js.setFilename("test.*");

    String jarName = js.find();
    System.out.println("jarname1 - > " + jarName);
    System.out.println("TEST2_JAR_FILE ->" + TEST2_JAR_FILE.getCanonicalPath());
    assertEquals(TEST2_JAR_FILE.getCanonicalPath(), jarName);

    js.setFilename("lib.*");
    jarName = js.find();
    System.out.println("jarName2 -> " + jarName);
    assertEquals(TEST_JAR_FILE.getCanonicalPath(), jarName);

  }

}
