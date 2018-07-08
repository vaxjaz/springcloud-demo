package com.jay.li.springcloud.zuul.util;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jay
 * @date 2018/6/22
 */
public final class GroovyUitls {

    private static final String filePathName = "D:\\groovy";
    private static final String getRoutes = "getRoutes";

    /**
     * 通过GroovyClassLoader访问groovy，返回路由规则
     *
     * @return
     */
    public static List<ZuulProperties.ZuulRoute> getRoutesByLoader() {
        List<ZuulProperties.ZuulRoute> rules = new LinkedList<>();
        //获取 groovy 类的反射对象
        GroovyClassLoader loader = new GroovyClassLoader();
        File groovyFile = new File(filePathName);
        File[] files = groovyFile.listFiles();
        Arrays.asList(files).stream()
                .filter(file -> file.exists() && file.getName().endsWith(".groovy"))
                .forEach(file -> {
                    try {
                        Class aClass = loader.parseClass(file);
                        GroovyObject groovyObj = (GroovyObject) aClass.newInstance();
                        Object getRoute = groovyObj.invokeMethod(getRoutes, null);
                        rules.add((ZuulProperties.ZuulRoute) convertMap(ZuulProperties.ZuulRoute.class, Bean2Map.bean2map(getRoute)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("groovy 文件操作异常！" + e.getMessage());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("groovy 文件格式不合法！" + e.getMessage());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        throw new RuntimeException("groovy 文件格式不合法！" + e.getMessage());
                    }
                });
        return rules;
    }

    /**
     * 通过groovyScriptEngine访问groovy文件，返回路由规则
     *
     * @return
     */
    public static List<ZuulProperties.ZuulRoute> getRoutesByScriptEngine() throws IOException {
        LinkedList<ZuulProperties.ZuulRoute> rules = new LinkedList<>();
        GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(filePathName);
        File groovyFile = new File(filePathName);
        File[] files = groovyFile.listFiles();
        Arrays.asList(files).stream()
                .filter(file -> file.exists() && file.getName().endsWith(".groovy"))
                .forEach(file -> {
                    Script script = null;
                    try {
                        script = groovyScriptEngine.createScript(file.getName(), new Binding());
                        Object routes = script.invokeMethod(getRoutes, null);
                        rules.add((ZuulProperties.ZuulRoute) convertMap(ZuulProperties.ZuulRoute.class, Bean2Map.bean2map(routes)));
                    } catch (ResourceException e) {
                        e.printStackTrace();
                        throw new RuntimeException("groovy脚本资源异常！" + e.getMessage());
                    } catch (ScriptException e) {
                        e.printStackTrace();
                        throw new RuntimeException("groovy脚本执行异常!" + e.getMessage());
                    }
                });
        return rules;
    }


    public static Object convertMap(Class type, Map map) {
        BeanInfo beanInfo = null; // 获取类属性
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new RuntimeException("不能获取" + type + "的属性信息！" + e.getMessage());
        }
        Object obj = null; // 创建 JavaBean 对象
        try {
            obj = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("对象实例化异常！" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("对象属性无法操作！" + e.getMessage());
        }
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                Object value = map.get(propertyName);
                try {
                    descriptor.getWriteMethod().invoke(obj, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("对象属性无法操作！" + e.getMessage());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException("对象属性无法操作！" + e.getMessage());
                }
            }
        }
        return obj;
    }
}
