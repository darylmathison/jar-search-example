package org.mathison.util;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class TestJarsSearch extends TestCase {

	public static final String ROOT_PATH = "./target/test-classes";
	public static final File TEST2_JAR_FILE = new File(ROOT_PATH, "jars/subJars/test2.jar");
	public static final File TEST_JAR_FILE = new File(ROOT_PATH, "jars/alltest.jar");
	
	public void testFind() {
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}

}
