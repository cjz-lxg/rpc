package lxg.cjz.rpc.common.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/11
 * @description
 */
public class ClassScanner {
    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";
    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";
    /**
     * class文件的后缀
     */
    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 扫描当前工程中指定包下的所有类信息
     * @param packageName 扫描的包名
     * @param packagePath 包在磁盘上的完整路径
     * @param recursive 是否递归调用
     * @param classNameList 类名称的集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<String> classNameList){
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        assert dirfiles != null;
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classNameList);
            }else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                //添加到集合中去
                classNameList.add(packageName + '.' + className);
            }
        }
    }

    /**
     * 扫描Jar文件中指定包下的所有类信息
     *
     * @param packageName    扫描的包名
     * @param classNameList  完成类名存放的List集合
     * @param recursive      是否递归调用
     * @param packageDirName 当前包名的前面部分的名称
     * @param url            包的url地址
     * @return 处理后的包名，以供下次调用使用
     * @throws IOException
     */
    private static String findAndAddClassesInPackageByJar(String packageName, List<String> classNameList, boolean recursive, String packageDirName, URL url) throws IOException {
        //读取建立jar文件
        JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        packageName = packageName.replace(".", "/");
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }
            if (name.startsWith(packageName)) {
                int idx = name.lastIndexOf('/');
                //TODO 这里有问题,if (idx != -1 || recursive) 的语义在这里已经被if (name.endsWith(CLASS_FILE_SUFFIX) && !jarEntry.isDirectory())覆盖了
                if (idx != -1 || recursive) {
                    if (name.endsWith(CLASS_FILE_SUFFIX) && !jarEntry.isDirectory()) {
                        String className = name.substring(0, name.length() - CLASS_FILE_SUFFIX.length()).replace("/", ".");
                        classNameList.add(className);
                    }
                }
            }
        }
        return packageName;
    }

    /**
     * 扫描指定包下的所有类信息
     * @param packageName 指定的包名
     * @return 指定包下所有完整类名的List集合
     * @throws Exception
     */
    public static List<String> getClassNameList(String packageName) throws Exception{
        //第一个class类的集合
        List<String> classNameList = new ArrayList<String>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        //循环迭代下去
        while (dirs.hasMoreElements()){
            //获取下一个元素
            URL url = dirs.nextElement();
            //得到协议的名称
            String protocol = url.getProtocol();
            //如果是以文件的形式保存在服务器上
            if (PROTOCOL_FILE.equals(protocol)) {
                //获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                //以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classNameList);
            } else if (PROTOCOL_JAR.equals(protocol)){
                packageName = findAndAddClassesInPackageByJar(packageName, classNameList, recursive, packageDirName, url);
            }
        }
        return classNameList;
    }
}
