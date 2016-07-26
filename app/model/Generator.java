package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import running.Global;


public class Generator {
	
	private Map<String, Class<?>> properties = new LinkedHashMap<>();
	
	private Class<?> classGenerate = Object.class;
	
	public Generator() {
		// TODO Auto-generated constructor stub
	}
	
	   public Class<?> generator(String name) throws CannotCompileException, NotFoundException{
		   ApplicationContext applicationContext = Global.getApplicationContext();
		   ReaderGenerique readerGenerique = applicationContext.getBean("readerGenerique", ReaderGenerique.class);
		   Class<?> rowObjectClass = null;
		   try {
			   if(readerGenerique.getExt().equals("csv")) {
				   System.out.println(" buildCsvClassName CSV ");
				   rowObjectClass = App.buildCSVClassName(this.properties, name);
				   classGenerate = rowObjectClass;
			   }else if(readerGenerique.getExt().equals("xml")){
				   System.out.println(" buildXmlClassName XML ");
				   rowObjectClass = App.buildCSVClassNamexml(this.properties, name);
				   classGenerate = rowObjectClass;
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
