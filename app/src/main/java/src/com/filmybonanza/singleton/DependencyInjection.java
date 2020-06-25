package src.com.filmybonanza.singleton;

import com.google.gson.Gson;

import lombok.Getter;
import src.com.filmybonanza.handler.EventHandler;
import src.com.filmybonanza.handler.UserHandler;
import src.com.filmybonanza.utils.UtilClass;

public class DependencyInjection {

    @Getter
    private static UserHandler userHandler=new UserHandler();

    @Getter
    private static EventHandler eventHandler=new EventHandler();

    @Getter
    public  static UtilClass utilClass=new UtilClass();

    @Getter
    public  static Gson gson=new Gson();

}
