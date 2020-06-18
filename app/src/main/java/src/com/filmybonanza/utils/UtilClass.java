package src.com.filmybonanza.utils;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import src.com.filmybonanza.singleton.DependencyInjection;

public class UtilClass {

    public <E> E singleResponseTransformer(Map<String, AttributeValue> item,Class<E> resclass)
    {
        Map<String,String>resmap=new HashMap<String,String>();

        for (Map.Entry<String, AttributeValue> pair : item.entrySet()) {
            resmap.put(pair.getKey(),pair.getValue().getS());
        }

        String json = DependencyInjection.getGson().toJson(resmap);
        return DependencyInjection.getGson().fromJson(json,resclass);

    }

    public <E> ArrayList<E> multipleResponseTransformer(ArrayList<Map<String,AttributeValue>>response ,Class<E> resclass )
    {
        ArrayList<E>finalarrayList=new ArrayList<>();
        for(Map<String,AttributeValue>map:response)
        {
            finalarrayList.add(singleResponseTransformer(map,resclass));
        }
        return finalarrayList;
    }
}
