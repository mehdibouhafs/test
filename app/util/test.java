package util;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class test {
	public static void main(String[] args) throws Exception {
		String chemin = "C:\\Users\\MBS\\Desktop\\complete\\src\\main\\resources\\sample-data.csv";
	    String[] fieldNames = null;
	    Class<?> rowObjectClass = null;
	    try(BufferedReader stream = new BufferedReader(new InputStreamReader(new FileInputStream(new File(chemin))))) {
	        while(true) {
	            String line = stream.readLine();
	            if(line == null) {
	                break;
	            }
	            if(line.isEmpty() || line.startsWith("#")) {
	                continue;
	            }
	            if(rowObjectClass == null) {
	                fieldNames = line.split(",");
	                rowObjectClass = buildCSVClass(fieldNames);
	            } else {
	                String[] values = line.split(",");
	                Object rowObject = rowObjectClass.newInstance();
	                for (int i = 0; i < fieldNames.length; i++) {
	                    Field f = rowObjectClass.getDeclaredField(fieldNames[i]);
	                    f.setAccessible(true);
	                    f.set(rowObject, values[i]);
	                }
	                System.out.println(reflectToString(rowObject));
	            }
	        }
	    }
	}

	private static int counter = 0;
	
	public static Class<?> buildCSVClass(String[] fieldNames) throws CannotCompileException, NotFoundException {
	    ClassPool pool = ClassPool.getDefault();
	    CtClass result = pool.makeClass("CSV_CLASS$" + (counter++));
	    ClassFile classFile = result.getClassFile();
	    ConstPool constPool = classFile.getConstPool();
	    classFile.setSuperclass(Object.class.getName());
	    for (String fieldName : fieldNames) {
	        CtField field = new CtField(ClassPool.getDefault().get(String.class.getName()), fieldName, result);
	        result.addField(field);
	    }
	    classFile.setVersionToJava5();
	    return result.toClass();
	}

	public static String reflectToString(Object value) throws IllegalAccessException {
	    StringBuilder result = new StringBuilder(value.getClass().getName());
	    result.append("@").append(System.identityHashCode(value)).append(" {");
	    for (Field f : value.getClass().getDeclaredFields()) {
	        f.setAccessible(true);
	        result.append("\n\t").append(f.getName()).append(" = ").append(f.get(value)).append(", ");
	    }
	    result.delete(result.length()-2, result.length());
	    return result.append("\n}").toString();
	}
}
