package org.example.contactdatabase.core.reflectutil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    private String packageName;

    public PackageScanner(String packageName) {
        this.packageName = packageName;
    }

    public List<Class> getClassesWithAnnotation(final Class<? extends Annotation> annotation) {
        List<Class> collect = new ArrayList<>();

        for (Class c : getAllClasses()) {
            if (c.isAnnotationPresent(annotation)) {
                collect.add(c);
            }
        }
        return collect;
    }

    private List<Class> getAllClasses() {
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            URI pathToFile = classLoader.getResource(path).toURI();
            return getAllInFolder(new File(pathToFile), packageName);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<Class> getAllInFolder(File folder, String packageName) {
        List<Class> classList = new ArrayList<>();
        if (!folder.exists()) return classList;
        for (File f : folder.listFiles()) {
            if (f.getName().endsWith(".class")) {
                String className = packageName + "." + f.getName().substring(0, f.getName().length() - 6);
                try {
                    classList.add(Class.forName(className));
                } catch (ClassNotFoundException ignored) {
                }
            } else if (f.isDirectory()) {
                classList.addAll(getAllInFolder(f, packageName + "." + f.getName()));
            }
        }
        return classList;
    }
}
