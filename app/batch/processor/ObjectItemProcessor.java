package batch.processor;

import batch.model.ReaderGenerique;
import batch.util.Generator;
import org.springframework.batch.item.ItemProcessor;
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
        ReaderGenerique readerGenerique = context.getBean("readerGenerique", ReaderGenerique.class);
        Generator generator = (Generator) context.getBean("generator");
        for (Map.Entry<String, Class<?>> entry : generator.getProperties().entrySet()) {
            try {
                Field f = item.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                switch (entry.getValue().getSimpleName()){
                    case "Integer":
                        int p =Integer.parseInt(f.get(item).toString());
                        break;
                    case "String":
                        String s = f.get(item)+"";
                        break;
                    case "Boolean":
                        Boolean b = (Boolean) f.get(item);
                        break;
                    case "Double":
                        Double b1 =  Double.parseDouble(f.get(item).toString());
                        break;
                    case "Float":
                        Float f1 =  Float.parseFloat(f.get(item).toString());
                        break;
                    case "Long":
                        Long l = Long.parseLong(f.get(item).toString());
                        break;
                    case "Date":
                        try {
                            java.util.Date date = (java.util.Date)(f.get(item));
                        } catch (IllegalAccessException e) {
                            readerGenerique.getErrors().put("casting Date",e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("DEFAULT");
                        break;
                }
            } catch (NoSuchFieldException e) {
                readerGenerique.getErrors().put("NoSuchFiledException",e.getMessage());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                readerGenerique.getErrors().put("IllegalAccessException",e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("PROCESSSSSSSSSSS"+ Generator.reflectToString(item));
        return item;
    }

}
