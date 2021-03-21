package week1.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;


@SpringBootApplication
public class XlassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        String className = "Hello";
        String methodName = "hello";
        ClassLoader classLoader = new XlassLoader();
        Class<?> loaderclass = classLoader.loadClass(className);
        Object instance = loaderclass.newInstance();
        Method method = loaderclass.getMethod(methodName);
        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Hello.xlass");
        try {
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            byte[] classBytes = decode(byteArray);
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            close(inputStream);
        }
    }

    private static byte[] decode(byte[] bytes) {
        byte[] tyteArray = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            tyteArray[i] = (byte) (255 - bytes[i]);
        }
        return tyteArray;
    }

    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
