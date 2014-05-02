package edu.naukma.reshet;

import edu.naukma.reshet.shared.MongoCache;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: reshet
 * Date: 3/4/14
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainMongo {
  public static void main(String args[]){
    AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();

    ctx.register(MongoConfiguration.class);
    ctx.register(IntegrationConfig.class);
    ctx.refresh();

    MongoCache cache = ctx.getBean(MongoCache.class);
    cache.saveTerm("ababababa");
    //MyService myService = ctx.getBean(MyService.class);
    //myService.doStuff();
    System.out.println("Master Thesis Ihor Reshetnev work 777");
  }
}
