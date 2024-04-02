import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 *TODO 내장톰캣 실행
 * 포트, path 설정
 */
public class WebServer {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        File file = new File(".");
        System.out.println(file.getAbsoluteFile());
        tomcat.addWebapp("", file.getAbsolutePath() + "/web");
        tomcat.start();
        tomcat.getServer().await(); // 요청대기
    }
}
