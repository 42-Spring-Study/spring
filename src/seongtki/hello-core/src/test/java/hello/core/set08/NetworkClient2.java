package hello.core.set08;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient2 implements InitializingBean, DisposableBean {
    private String url;
    public NetworkClient2() {
        System.out.println("생성자 호출, url = " + url);
    }
    public void setUrl(String url) {
        this.url = url;
    }
    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }
    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }
    //서비스 종료시 호출
    public void disConnect() {
        System.out.println("close + " + url);
    }
    @Override
    public void destroy() throws Exception {
        disConnect();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결 메시지");
    }
}
