import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

public class HelloXlassClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        Class<?> hello = new HelloXlassClassLoader().loadClass("Hello");
        Method helloMethod = hello.getDeclaredMethod("hello");
        helloMethod.invoke(hello.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = getBytesByFile();
        convert(bytes, bytes.length);
        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * 获取文件字节数组
     *
     * @return
     */
    private byte[] getBytesByFile() {
        // 在当前文件路径下查找Hello.xlass文件，并加载
        String filePath = HelloXlassClassLoader.class.getResource("Hello.xlass").getPath();
        File file = new File(filePath);
        byte[] bytes = null;
        try (
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ) {
            byte[] readBytes = new byte[1024];
            int size;
            while ((size = fis.read(readBytes)) != -1) {
                bos.write(readBytes, 0, size);
            }
            bytes = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 每个字节反转
     *
     * @param bs
     * @param len
     */
    private static void convert(byte[] bs, int len) {
        for (int i = 0; i < len; i++)
            bs[i] = (byte) ~bs[i];
    }
}
