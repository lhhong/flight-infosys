package com.czce4013.marshaller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MarshallerTest {

    private static final Logger logger = LoggerFactory.getLogger(MarshallerTest.class);

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class TestClass extends Marshallable{
        @FieldId(0)
        String a;
        @FieldId(2)
        int b;
        @FieldId(3)
        List<Integer> c;
        @FieldId(4)
        TestSubClass d;

        @JSONField(serialize = false, deserialize = false)
        double noSerialize;

        @Override
        public String toString() {
            return "TestClass{" +
                    "a='" + a + '\'' +
                    ", b=" + b +
                    ", c=" + c +
                    ", d=" + d +
                    ", noSerialize=" + noSerialize +
                    '}';
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class TestSubClass {
        @FieldId(0)
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
        TestClass cObject = new TestClass("string-a", 2, l, scObject, 1.23);

        byte[] byteList = cObject.marshall();
        TestClass recast = Marshallable.unmarshall(byteList, TestClass.class);

        logger.info("old: {}", cObject);
        logger.info("new: {}", recast);
        assert JSON.toJSON(cObject).equals(JSON.toJSON(recast));

    }

}
