package running;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import play.Application;
import play.GlobalSettings;
import play.Logger;


/**
 * Created by MBS on 27/06/2016.
 */
public class Global extends GlobalSettings {

    private static ConfigurableApplicationContext applicationContext;
    String[] springConfig  =
            {
                    "JobConfig/jobCsv.xml",
                    "JobConfig/jobXML.xml"

            };

    @Override
    public void onStart(Application app) {

        //Normal c = new Normal();
        //controllers.Application application = (controllers.Application) context.getBean("application");
        applicationContext = new ClassPathXmlApplicationContext(springConfig);
        Logger.info("Application has started by Mehdi diricted by MIMO");
    }


    @Override
    public void onStop(Application app) {
        super.onStop(app);
        if (applicationContext != null) {
            applicationContext.close();
            Logger.info("Application closed... by Mehdi diricted by MIMO");
        }
    }

    public static ApplicationContext getApplicationContext() {

        return applicationContext;
    }


    public static <T> T getBean(Class<T> beanClass) {
        if (applicationContext == null) {
            throw new IllegalStateException("application context is not initialized");
        }
        return applicationContext.getBean(beanClass);
    }


}
