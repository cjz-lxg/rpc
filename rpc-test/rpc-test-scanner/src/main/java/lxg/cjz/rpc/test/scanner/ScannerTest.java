package lxg.cjz.rpc.test.scanner;

import lxg.cjz.rpc.common.scanner.ClassScanner;
import lxg.cjz.rpc.common.scanner.reference.RpcReferenceScanner;
import lxg.cjz.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/11
 * @description
 */
public class ScannerTest {

    public static void main(String[] args) throws Exception {
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("org/junit");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            System.out.println(url);
        }
    }


    @Test
    public void testJar() throws IOException {

    }

    @Test
    public void testFile() throws Exception {
        //绝对路径和module相对路径可以被识别
        System.out.println(new File("src/main/java/lxg/cjz/rpc/test/scanner").isDirectory());
        //content目录下的资源无法识别
        System.out.println(new File("io/binghe/rpc/test/scanner").isDirectory());
    }

    @Test
    public void testScannerClassNameList() throws Exception {
        List<String> classNameList = ClassScanner.getClassNameList("lxg.cjz.rpc.test.scanner");
        classNameList.forEach(System.out::println);
        classNameList = ClassScanner.getClassNameList("org.slf4j");
        classNameList.forEach(System.out::println);
    }

    @Test
    public void testScannerClassNameListByRpcService() throws Exception {
        RpcServiceScanner.
                doScannerWithRpcServiceAnnotationFilterAndRegistryService("lxg.cjz.rpc.test.scanner");
        RpcServiceScanner.
                doScannerWithRpcServiceAnnotationFilterAndRegistryService("org.slf4j");
    }

    @Test
    public void testScannerClassNameListByRpcReference() throws Exception {
        RpcReferenceScanner.
                doScannerWithRpcReferenceAnnotationFilter("lxg.cjz.rpc.test.scanner");
    }

}

