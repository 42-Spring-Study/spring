package hello.core.sec09.web;

import hello.core.sec09.common.MyLogger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogDemoService {

    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public LogDemoService(MyLogger myLogger) {
        this.myLogger = myLogger;
    }

//    @Autowired
//    public LogDemoService(ObjectProvider<MyLogger> myLoggerProvider) {
//        this.myLoggerProvider = myLoggerProvider;
//    }

    public void logic(String id) {
        myLogger.log("service id = " + id);
//        myLoggerProvider.getObject().log("service id = " + id);
    }
}
