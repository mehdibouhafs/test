package util;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javassist.*;
import org.jdom2.CDATA;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
/**
 * Hello world!
 *
 */
public class App 
{
	   public App() {
	}
	   private static int counter = 0;
	   public static Class<?> buildCSVClass(Map<String, Class<?>> properties) throws CannotCompileException, NotFoundException {
		   System.out.println("BuildCsvClass UTIL");
		   ClassPool pool = new ClassPool(true);//ClassPool.getDefault();
		   CtClass result = pool.makeClass("listeners.CSV_CLASS$" + (counter));
		   counter++;
		   result.setSuperclass(pool.get((Serializable.class).getName()));
		   ClassFile classFile = result.getClassFile();
		   ConstPool constPool = classFile.getConstPool();
		   classFile.setSuperclass(Object.class.getName());
	       for (Entry<String, Class<?>> entry : properties.entrySet()) {
	           CtField field = new CtField(ClassPool.getDefault().get(entry.getValue().getName()), entry.getKey(), result);
			   CtMethod setter =  CtNewMethod.setter("set"+entry.getKey(), field);
			   CtMethod getter =  CtNewMethod.getter("get"+entry.getKey(), field);
			   result.addField(field);
			   result.addMethod(setter);
			   result.addMethod(getter);
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
	   
	   public static void setField(String field,Object value,Object object){
			try {
				Field f = object.getClass().getDeclaredField(field);
				f.set(object,value);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	   
	   public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, CannotCompileException, NotFoundException, IllegalArgumentException, ParseException {
		   Map<String, Class<?>> properties = new HashMap<>();
			   properties.put("id", Integer.class);
			   properties.put("firstName", String.class);
			   properties.put("Date", Date.class);
			/*  try {
				Class<?> rowObjectClass = App.buildCSVClass(properties);
				Object rowObject = rowObjectClass.newInstance();
				 Field f = rowObjectClass.getDeclaredField("id");
				 f.setAccessible(true);
	             f.set(rowObject,1);
				System.out.println(reflectToString(rowObject));
			} catch (CannotCompileException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		   for (int i=0;i<3;i++) {
			   Class<?> cla = App.buildCSVClass(properties);
			   Object o = cla.newInstance();
			   Field f6 =  o.getClass().getDeclaredField("id");
			   f6.set(o, i);
			   System.out.println("---------"+App.reflectToString(o));
		   }

	}
	   
		
		
		
		
		


	    

	    

	   
	
	
}
