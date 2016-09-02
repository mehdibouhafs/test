package batch.processor;
import batch.model.InputError;
import batch.util.Generator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ParseException;
import org.springframework.context.ApplicationContext;
import running.Global;

import java.lang.reflect.Field;
import java.util.Map;
/**
 * Created by MBS on 05/08/2016.
 */

public class ObjectItemProcessor implements ItemProcessor<Object,Object> {
    @Override
    public Object process(Object item) throws Exception {

        ApplicationContext context = Global.getApplicationContext();
        Generator generator = (Generator) context.getBean("generator");
        InputError inputError = (InputError) context.getBean("InputError");
        for (Map.Entry<String, Class<?>> entry : generator.getProperties().entrySet()) {
            try {
                Field f = item.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                switch (entry.getValue().getSimpleName()){
                    case "Integer":
                        try {
                            int p = Integer.parseInt(f.get(item).toString());
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                        break;
                    case "String":
                        try {
                            String s = f.get(item) + "";
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                        break;
                    case "Boolean":
                        try {
                            Boolean b = (Boolean) f.get(item);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                        break;
                    case "Double":
                        try {
                            Double b1 = Double.parseDouble(f.get(item).toString());
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                        break;
                    case "Float":
                        try {
                            Float f1 =  Float.parseFloat(f.get(item).toString());
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        break;
                    case "Long":
                        Long l = Long.parseLong(f.get(item).toString());
                        break;
                    case "Date":
                        try {
                            java.util.Date date = (java.util.Date)(f.get(item));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("PROCESSSSSSSSSSS"+ Generator.reflectToString(item));
        return item;
    }

}
