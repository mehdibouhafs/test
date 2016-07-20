package model;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.avaje.ebean.Model;
import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
public class App
	{
		public App() {
		}
		private static int counter = 0;
		public static Class<?> buildCSVClass(Map<String, Class<?>> properties) throws CannotCompileException, NotFoundException, IOException {
			ClassPool pool = new ClassPool(true);//ClassPool.getDefault();
			CtClass result = pool.makeClass("model.CSV_CLASS$" + (counter));
			counter++;
			result.setSuperclass(pool.get((Serializable.class).getName()));
			ClassFile classFile = result.getClassFile();
			ConstPool constPool = classFile.getConstPool();
			classFile.setSuperclass(Object.class.getName());
			for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
				CtField field = new CtField(ClassPool.getDefault().get(entry.getValue().getName()), entry.getKey(), result);
				CtMethod setter =  CtNewMethod.setter("set"+entry.getKey(), field);
				CtMethod getter =  CtNewMethod.getter("get"+entry.getKey(), field);
				result.addField(field);
				result.addMethod(setter);
				result.addMethod(getter);
			}
			classFile.setVersionToJava5();
			result.writeFile();
			result.defrost();
			result.detach();
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

		public static Object getField(String field,Object object){
			try {
				Field f = object.getClass().getDeclaredField(field);
				return  f.get(field);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return  null;
		}


}







