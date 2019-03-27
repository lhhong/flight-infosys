package com.czce4013.marshaller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class MarshallerTest {

    private static final Logger logger = LoggerFactory.getLogger(MarshallerTest.class);

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class TestClass extends Marshallable{

        String a;
        int b;
        List<Integer> c;
        TestSubClass d;
        double e;

        // Will not serialize data in IgnoreField
        @IgnoreField
        // This annotation is just for JSON testing to check the code, not needed in entities
        @JSONField(serialize = false, deserialize = false)
        double noSerialize;

        @Override
        public String toString() {
            return "TestClass{" +
                    "a='" + a + '\'' +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    ", e=" + e +
                    ", noSerialize=" + noSerialize +
                    '}';
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class TestSubClass {
        int a;

        @Override
        public String toString() {
            return "TestSubClass{" +
                    "a=" + a +
                    '}';
        }
    }

    @Test
    public void testMarshall() {
        TestSubClass scObject = new TestSubClass(4);
        List<Integer> l = Arrays.asList(6,7,8);
        TestClass cObject = new TestClass("string-a", 2, l, scObject, 4.32, 1.23);

        byte[] byteList = cObject.marshall();
        TestClass recast = Marshallable.unmarshall(byteList, TestClass.class);

        // Another way to use unmarshall if you don't know the class type
        TestClass recast2 = null;
        Marshallable m = Marshallable.unmarshall(byteList);
        if (m instanceof TestClass) {
            recast2 = (TestClass) m;
        }

        logger.info("original: {}", cObject);
        logger.info("recast: {}", recast);
        logger.info("recast2: {}", recast2);
        assert JSON.toJSON(cObject).equals(JSON.toJSON(recast));
        assert JSON.toJSON(cObject).equals(JSON.toJSON(recast2));

    }

}
