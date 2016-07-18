package model;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.avaje.ebean.Model;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;


import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;

/**
 * Hello world!
 *
 */
public class App {

	public App() {
	}

	public static Object createBeanClass(
			    /* fully qualified class name */
				final String className,
			    /* bean properties, name -> type */
				final Map<String, Class<?>> properties) throws ClassNotFoundException {
		System.out.println("property create bean"+properties);

		System.out.println("className"+className);
		final BeanGenerator beanGenerator = new BeanGenerator();

			    /* use our own hard coded class name instead of a real naming policy */
		beanGenerator.setNamingPolicy(new NamingPolicy() {
			public String getClassName(final String prefix,
									   final String source, final Object key, final Predicate names) {
				return className;
			}
		});
		BeanGenerator.addProperties(beanGenerator, properties);
		beanGenerator.setSuperclass(Model.class);
		beanGenerator.createClass();
		try {
			return (Class<?>) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return  null;
	}

	final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

	final java.util.Random rand = new java.util.Random();

	// consider using a Map<String,Boolean> to say whether the identifier is being used or not
	final Set<String> identifiers = new HashSet<>();

	public  String randomIdentifier() {
		StringBuilder builder = new StringBuilder();
		while(builder.toString().length() == 0) {
			int length = rand.nextInt(5)+5;
			for(int i = 0; i < length; i++)
				builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
			if(identifiers.contains(builder.toString()))
				builder = new StringBuilder();
		}
		return builder.toString();
	}

	public Class<?> SettersBean(Map<String, Class<?>> properties, Object o, FieldSet fieldSet) {
		int i = 0;
		for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
			try {
				String cap = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
				Method set = o.getClass().getMethod("set" + cap, entry.getValue());
				switch (entry.getValue().getSimpleName()) {
					case "String":
						try {
							set.invoke(o, fieldSet.readString(i));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						break;
					case "Integer":
						try {
							set.invoke(o, fieldSet.readInt(i));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						break;
					case "Date":
						try {
							set.invoke(o, fieldSet.readDate(i));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					default:
						break;
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			i++;
		}
		return (Class<?>) o;
	}


}







