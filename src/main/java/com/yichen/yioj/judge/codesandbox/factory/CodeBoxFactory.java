package com.yichen.yioj.judge.codesandbox.factory;

import com.yichen.yioj.judge.codesandbox.CodeSandBox;
import com.yichen.yioj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yichen.yioj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yichen.yioj.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import org.springframework.stereotype.Component;

@Component
public class CodeBoxFactory {
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
