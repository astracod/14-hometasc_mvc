package org.example.contactdatabase.core.reflectutil;

import org.example.contactdatabase.core.annotations.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    Map<Class, Object> instanceMap = new HashMap<>();
    List<Class> classList;

    public Context() {
        this.classList = new PackageScanner("org.example.contactdatabase").getClassesWithAnnotation(Component.class);
    }

    synchronized public Object getInstance(Class c) {

        for (Class aClass : instanceMap.keySet()) {
            if (c.isAssignableFrom(aClass)) {
                return instanceMap.get(aClass);
            }
        }

        for (Class aClass : classList) {
            if (c.isAssignableFrom(aClass)) {
                try {
                    Object instance = aClass.getConstructor().newInstance();
                    instanceMap.put(c, instance);
                    return instance;
                } catch (Exception e) {
                    System.out.println("error");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
