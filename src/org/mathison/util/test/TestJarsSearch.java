package org.mathison.util.test;

import java.io.File;
import java.io.IOException;

import org.mathison.util.JarsSearch;

import junit.framework.TestCase;

public class TestJarsSearch extends TestCase {

	public static final String ROOT_PATH = "classes/org/mathison/util/test";
	public static final File TEST2_JAR_FILE = new File(ROOT_PATH, "jars/subJars/test2.jar");
	public static final File TEST_JAR_FILE = new File(ROOT_PATH, "jars/alltest.jar");
	
	public void testFind() {
		try {
			
			JarsSearch js = new JarsSearch();
			js.setRootpath(ROOT_PATH);
			js.setFilename("test.*");
			
			String jarName = js.find();
			assertEquals(TEST2_JAR_FILE.getCanonicalPath(), jarName);
			
			js.setFilename("lib.*");
			jarName = js.find();
			assertEquals(TEST_JAR_FILE.getCanonicalPath(), jarName);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}

}
