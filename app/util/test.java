package util;

import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class test {
	public static void main(String[] args) throws Exception {
		String chemin = "C:/Users/MBS/Desktop/complete/src/main/resources/sample-data.csv";
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
	                rowObjectClass = buildPersonClass(new HashMap<>());
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

	private static int counter = 5;

	public static Class<?> buildPersonClass(Map<String, Class<?>> properties) throws CannotCompileException, NotFoundException {
		System.out.println("Test Build PErson Object");
		ClassPool pool = new ClassPool(true);//ClassPool.getDefault();
		CtClass result = pool.makeClass("model.Person$" + (counter++));
		ClassFile classFile = result.getClassFile();
		ConstPool constPool = classFile.getConstPool();
		classFile.setSuperclass(Object.class.getName());
		for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
			CtField field = new CtField(ClassPool.getDefault().get(entry.getValue().getName()), entry.getKey(), result);
			result.addField(field);
		}
		classFile.setVersionToJava5();
		System.out.println("Result0"+result.toClass().toString());
		System.out.println("Result0"+result.toClass());
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
