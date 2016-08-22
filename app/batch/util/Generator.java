package batch.util;

import batch.model.Reader;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.springframework.context.ApplicationContext;
import running.Global;

import javax.xml.bind.annotation.XmlAccessType;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by MBS on 20/07/2016.
 *
 */
public class Generator {
		private Map<String, Class<?>> properties = new LinkedHashMap<>();
		private Class<?> classGenerate = Object.class;
		private static int counter2 = 0;
		private ApplicationContext applicationContext = Global.getApplicationContext();

	public Generator() {
		}

	public static void main(String[] args) {
		Class c = Generator.getClass("app.batch.generate.A.class");
	}

		public static Class<?> getClass(String name){
			/*ClassPool pool = ClassPool.getDefault();
			try {
				Class<?> c = pool.get(name).toClass();
				return  c;
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (CannotCompileException e) {
				e.printStackTrace();
			}
			return null;*/
			ClassLoader classLoader = Generator.class.getClassLoader();
			try {
				Class aClass = classLoader.loadClass(name);
				return aClass;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}

		public Class<?> generator(String name, String typeXml) throws CannotCompileException, NotFoundException{
			Class<?> rowObjectClass = null;
			try {
				rowObjectClass = Generator.buildCSVClassNamexml(this.properties, name,typeXml);
				this.classGenerate = rowObjectClass;
				return rowObjectClass;

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	public static Class<?> buildCSVClassNamexml(Map<String, Class<?>> properties,String classeName,String typeXml) throws CannotCompileException, NotFoundException, IOException {
		ApplicationContext context = Global.getApplicationContext();
		ClassPool pool = ClassPool.getDefault();
		Reader reader =context.getBean("reader",Reader.class);
		CtClass result;
		String classeN;
		if(typeXml.equals("csv")){
			result = pool.makeClass("app.batch.generate."+classeName+"csv$"+counter2);
		}else {
			//classeN = readerGenerique.getFragmentRootElementName();
			result = pool.makeClass("app.batch.generate."+classeName +"xml$"+counter2);
		}
		counter2++;
		result.setSuperclass(pool.get((Serializable.class).getName()));
		ClassFile classFile = result.getClassFile();
		ConstPool constPool = classFile.getConstPool();
		classFile.setSuperclass(Object.class.getName());
		AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		System.out.println("Generator type"+typeXml);
		if(!typeXml.equals("csv")) {
			Annotation annot = new Annotation("javax.xml.bind.annotation.XmlRootElement", constPool);
			annot.addMemberValue("name", new StringMemberValue(reader.fragmentRootName, classFile.getConstPool()));
			attr.addAnnotation(annot);
		}
		Annotation annotation= new Annotation("javax.xml.bind.annotation.XmlAccessorType",constPool);
		EnumMemberValue emb=new EnumMemberValue(constPool);
		emb.setType(XmlAccessType.class.getName());
		emb.setValue("FIELD");
		annotation.addMemberValue("value",emb);
		attr.addAnnotation(annotation);
		classFile.addAttribute(attr);
		for (Map.Entry<String, Class<?>> entry : properties.entrySet()) {
			CtField field = new CtField(ClassPool.getDefault().get(entry.getValue().getName()), entry.getKey(), result);
			field.setModifiers(Modifier.PRIVATE);
			CtMethod setter =  CtNewMethod.setter("set"+entry.getKey(), field);
			CtMethod getter =  CtNewMethod.getter("get"+entry.getKey(), field);
			result.addMethod(setter);
			result.addMethod(getter);
			AnnotationsAttribute attra = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
					if(typeXml.equals("xml")){
						if(reader.cols.contains(entry.getKey())) {
							Annotation annota = new Annotation("javax.xml.bind.annotation.XmlElement", constPool);
							annota.addMemberValue("name", new StringMemberValue(entry.getKey(), classFile.getConstPool()));
							attra.addAnnotation(annota);
						}else {
							Annotation annota = new Annotation("javax.xml.bind.annotation.XmlAttribute", constPool);
							annota.addMemberValue("name", new StringMemberValue(entry.getKey(), classFile.getConstPool()));
							annota.addMemberValue("required",new BooleanMemberValue(false,classFile.getConstPool()));
							attra.addAnnotation(annota);
						}
					}
			if(entry.getValue()== Date.class){
				Annotation annota1 = new Annotation("javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter", constPool);
				annota1.addMemberValue("type", new ClassMemberValue("batch.model.adapters.LocalDateAdapter",classFile.getConstPool()));
				annota1.addMemberValue("value", new ClassMemberValue("batch.model.adapters.LocalDateAdapter",classFile.getConstPool()));
				attra.addAnnotation(annota1);
			}
			field.getFieldInfo().addAttribute(attra);
			result.addField(field);
		}
		classFile.setVersionToJava5();
		result.defrost();
		result.writeFile();
		result.detach();
		return result.toClass();
	}

		/*public static Object generatorBean(Map<String,Class<?>> properties,String className) throws IOException, CannotCompileException, NotFoundException, IllegalAccessException {
			Class<?> cla = Generator.buildCSVClassName(properties,className);
			Object o = null;
			try {
				o = cla.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return o;
		}*/

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

		public static int getCounter2() {
			return counter2;
		}

		public Map<String, Class<?>> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, Class<?>> properties) {
			this.properties = properties;
		}

		public Class<?> getClassGenerate() {
			return classGenerate;
		}

		public void setClassGenerate(Class<?> classGenerate) {
			this.classGenerate = classGenerate;
		}

		public static void setCounter2(int counter2) {
			Generator.counter2 = counter2;
		}
	}

