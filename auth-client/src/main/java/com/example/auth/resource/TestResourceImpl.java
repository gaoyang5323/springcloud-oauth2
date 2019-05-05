package com.example.auth.resource;

import com.kakuiwong.auth.TestResource;
import org.springframework.stereotype.Service;

@Service
public class TestResourceImpl implements TestResource {

    @Override
    public String test(String var) {
        switch (var) {
            case "1":
                return "你好";
            case "2":
                return "再见";
            default:
                return "听不懂";
        }
    }
}
