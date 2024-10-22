package com.yichen.yioj.judge.codesandbox;

import com.yichen.yioj.judge.codesandbox.factory.CodeBoxFactory;
import com.yichen.yioj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yichen.yioj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yichen.yioj.judge.codesandbox.proxy.CodeSandBoxProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSandBoxTest {
    /**
     * 没有使用 设计模式 耦合度高 每次都需要改接口
     */
    @Test
    public void testCodeSandBox() {
        CodeSandBox codeSandBox = new ExampleCodeSandbox();
        String code = "int main(){return 0;}";
        String language = "java";
        List<String> inputList = Arrays.asList("1+1", "2+3", "5+5");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        Assertions.assertNull(codeSandBox.executeCode(executeCodeRequest));
    }
    /**
     * 使用工厂模式 根据用户传入的字符串参数， 来生成对应的代码沙箱实现类
     */
    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    public void testFactoryCodeSandBox() {
        CodeSandBox codeSandBox = CodeBoxFactory.newInstance(type);
        String code = "int main(){return 0;}";
        String language = "java";
        List<String> inputList = Arrays.asList("1+1", "2+3", "5+5");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        Assertions.assertNull(codeSandBox.executeCode(executeCodeRequest));
    }

    /**
     * 使用代理模式增强沙箱功能 测试
     */
    @Test
    public void testProxyCodeSandBox() {
        // 使用代理模式调用远程沙箱
        CodeSandBox codeSandBox = CodeBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果: \" + (a + b));\n" +
                "    }\n" +
                "}";
        String language = "java";
        List<String> inputList = Arrays.asList("1 1", "2 2", "5 666");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
//        Assertions.assertNull(codeSandBoxProxy.executeCode(executeCodeRequest));
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}