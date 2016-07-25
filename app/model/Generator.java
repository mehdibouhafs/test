package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javassist.CannotCompileException;
import javassist.NotFoundException;



public class Generator {
	
	private Map<String, Class<?>> properties = new LinkedHashMap<>();
	
	private Class<?> classGenerate = Object.class;
	
	public Generator() {
		// TODO Auto-generated constructor stub
	}
	
	   public Class<?> generator(String name) throws CannotCompileException, NotFoundException{

		   Class<?> rowObjectClass = null;
		   try {
			   rowObjectClass = App.buildCSVClassName(this.properties,name);
			   this.classGenerate = rowObjectClass;
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
