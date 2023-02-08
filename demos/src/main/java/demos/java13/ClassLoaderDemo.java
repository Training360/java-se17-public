package demos.java13;

import javax.sql.DataSource;

public class ClassLoaderDemo {

    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader());
        System.out.println(DataSource.class.getClassLoader());
        System.out.println(ClassLoaderDemo.class.getClassLoader());

        var layer = ModuleLayer.boot();
        layer.modules().forEach(module -> {
            var name = module.getName();
            var classLoader = module.getClassLoader();
            System.out.println(name + " - " + classLoader);
        });
    }
}
