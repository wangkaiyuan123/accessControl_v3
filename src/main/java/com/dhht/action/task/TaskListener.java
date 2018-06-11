package com.dhht.action.task;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TaskListener implements  ServletContextListener{
	 public void contextInitialized(ServletContextEvent sce) {
		 System.out.println("tomcat启动时执行");
         new TimerManager();
    }
 
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        System.out.println("tomcat销毁时执行");
    }
}
