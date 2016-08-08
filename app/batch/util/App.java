package batch.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import batch.model.ReaderGenerique;
import org.springframework.context.ApplicationContext;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import running.Global;


public class App {
	
	private Map<String, Class<?>> properties = new LinkedHashMap<>();
	
	private Class<?> classGenerate = Object.class;
	
	public App() {
		// TODO Auto-generated constructor stub
	}
	   public Class<?> generator(String name, String typeXml) throws CannotCompileException, NotFoundException{
		   ApplicationContext applicationContext = Global.getApplicationContext();
		   ReaderGenerique readerGenerique = applicationContext.getBean("readerGenerique", ReaderGenerique.class);
		   Class<?> rowObjectClass = null;
		   try {
			   if(readerGenerique.getExt().equals("csv")) {
				   System.out.println(" buildCsvClassName CSV ");
				   rowObjectClass = Generator.buildCSVClassName(this.properties, name);
				   this.classGenerate = rowObjectClass;
			   }else if(readerGenerique.getExt().equals("xml")){
				   System.out.println(" buildXmlClassName XML " + this.properties);
				   if(!typeXml.equals("type3")){
					   rowObjectClass = Generator.buildCSVClassNamexml(this.properties, name, typeXml);
					   this.classGenerate = rowObjectClass;
				   }else {
					   rowObjectClass = Generator.buildCSVClassNamexml(this.properties, name,typeXml);
					   this.classGenerate = rowObjectClass;
				   }
			   }
			   return rowObjectClass;
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
		   return null;
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
	
	
	   
	   
	
	
}	
