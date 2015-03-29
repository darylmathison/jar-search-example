package org.mathison.util;

import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JarsSearch {

	public static final String FILE_PARAM = "--file";
	public static final String ROOT_PARAM = "--root";
	public static final String HELP_PARAM_PATTERN = "-? | --help";
	
	public static final String DEFAULT_ROOT = ".";
	
	private String filename;
	private String rootpath = DEFAULT_ROOT;
	
	public JarsSearch() {}
	
	public JarsSearch(String root, String filename) {
		setRootpath(root);
		setFilename(filename);
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the rootpath
	 */
	public String getRootpath() {
		return rootpath;
	}

	/**
	 * @param rootpath the rootpath to set
	 */
	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}

	/** 
	 * looks for the file on any jar under the rootpath 
	 * 
	 * @return String the path of the containing jar file
	 */ 
	public String find() {
		String jarFilename = null;
		
		try {
			File[] jarsNdirs = new File(getRootpath()).listFiles(new JarFileFilter());
			
			Arrays.sort(jarsNdirs, new ListingComparator());
			for (File f: jarsNdirs) {
				if (f.isDirectory()) {
					JarsSearch js = new JarsSearch(f.getCanonicalPath(), getFilename());
					jarFilename = js.find();
					if (Utils.isStringValid(jarFilename)) {
						break;
					}
				}
				else { // if not a directory, then a jar file
					
					JarEntry entry;
					Pattern p = Pattern.compile(getFilename());
					Matcher m = null;
					
					JarFile jarFile = new JarFile(f, false, JarFile.OPEN_READ);
					Enumeration <JarEntry> e = jarFile.entries();
					while (e.hasMoreElements()) {
						entry = e.nextElement();
						//System.out.println("printing entry name: " + entry.getName());
						m = p.matcher(entry.getName());
						if (m.matches()) {
							jarFilename = f.getCanonicalPath();
						}
					}
					jarFile.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jarFilename;
	}
	
	public String toString() {
		return "JarsSearch[ filename pattern - " + getFilename() + ", root path - " + getRootpath() + " ]";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filepattern = null;
		String rootdir = null;
		
		for(int i = 0; i < args.length; i++) {
			if (args[i].matches(HELP_PARAM_PATTERN)) {
				showArgs();
			} else if (args[i].matches(FILE_PARAM)) {
				if ((i + 1) < args.length) {
					filepattern = args[i + 1];
					i++;
					continue;
				}
			} else if (args[i].matches(ROOT_PARAM)) {
				if ((i + 1) < args.length) {
					rootdir = args[i + 1];
					i++;
					continue;
				}
			} else {
				showArgs();
			}
		}
		
		JarsSearch js = new JarsSearch();
		if (!Utils.isStringValid(filepattern) || ROOT_PARAM.equals(filepattern)) {
			showArgs();
		} else {
			js.setFilename(filepattern.trim());
		}
		if (Utils.isStringValid(rootdir) && !FILE_PARAM.equals(rootdir)) {
			js.setRootpath(rootdir.trim());
		}
		
		System.out.println(js.find());
		System.exit(0);
	}
	
	protected static void showArgs() {
		System.out.println("--help: this listing");
		System.out.println("-?    : this listing");
		System.out.println(ROOT_PARAM + ": where to start looking from.  Default = '.'");
		System.out.println(FILE_PARAM + ": regex pattern of a file to look for in a jar file");
		System.exit(0);
	}
}

class JarFileFilter implements java.io.FileFilter {

	public boolean accept(File arg0) {
		String name = arg0.getName();
		
		return ( arg0.canRead() && ( arg0.isDirectory() || name.endsWith("jar") ) );
	}
	
}

class ListingComparator implements java.util.Comparator<File> {

	public int compare(File arg0, File arg1) {
		if (arg0.isDirectory() && !arg1.isDirectory()) {
			return -1;
		} else if (!arg0.isDirectory() && arg1.isDirectory()) {
			return 1;
		} else { // ( (arg0.isDirectory() && arg1.isDirectory()) || (arg0.isFile() && arg1.isFile()) )
			return arg0.compareTo(arg1);
		}
	}
	
}
