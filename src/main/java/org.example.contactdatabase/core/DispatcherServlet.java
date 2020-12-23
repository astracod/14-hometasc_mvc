package org.example.contactdatabase.core;

import org.example.contactdatabase.core.annotations.Autowired;
import org.example.contactdatabase.core.annotations.Controller;
import org.example.contactdatabase.core.annotations.GetMapping;
import org.example.contactdatabase.core.annotations.PostMapping;
import org.example.contactdatabase.core.reflectutil.Context;
import org.example.contactdatabase.core.reflectutil.PackageScanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {


    private Context ctx = new Context();

    private boolean callControllerMethod(Class controller, HttpServletRequest req, HttpServletResponse resp) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String uri = req.getRequestURI();
        String method = req.getMethod();
        String baseurl = req.getContextPath();

        Method[] methods = controller.getMethods();
        String testUrl = null;

        for (Method m : methods) {
            if (m.isAnnotationPresent(GetMapping.class) && method.equals("GET")) {
                GetMapping annotation = m.getAnnotation(GetMapping.class);
                testUrl = annotation.value();
            } else if (m.isAnnotationPresent(PostMapping.class) && method.equals("POST")) {
                PostMapping annotation = m.getAnnotation(PostMapping.class);
                testUrl = annotation.value();
            }

//            System.out.println(testUrl + baseurl+" -> "+uri);


            if (testUrl != null &&  (baseurl + testUrl).equals(uri)) {


                Object o = controller.getConstructor().newInstance();
                for (Field f : controller.getDeclaredFields()) {

                    if (f.isAnnotationPresent(Autowired.class)) {

                        f.setAccessible(true);
                        Object val = ctx.getInstance(f.getType());
                        if (val != null) {
                            f.set(o, val);
                        }
                    }
                }
                m.invoke(o, req, resp);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PackageScanner packageScanner = new PackageScanner("org.example.contactdatabase");


        try {
            List<Class> controllers = packageScanner.getClassesWithAnnotation(Controller.class);

            boolean isFind = false;

            for (Class controller : controllers) {
                if (callControllerMethod(controller, req, resp)) {
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("404 Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}









