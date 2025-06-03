package user.com.user.handler;

import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import user.com.user.module.GoogleModule;
@Component
public class GoogleHandler {

    @Autowired
    private GoogleModule googleModule;
    public Map<String, String> checkToken(String code) throws ParseException {
        return googleModule.getToken(code);
    }
}
