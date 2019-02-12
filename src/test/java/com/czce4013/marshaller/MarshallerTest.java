package com.czce4013.marshaller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MarshallerTest {

    @AllArgsConstructor
    class TestClass {
        @FieldId(0)
        String a;
        @FieldId(2)
        int b;
        @FieldId(3)
        List<Integer> c;
        @FieldId(4)
        TestSubClass d;
    }

    @AllArgsConstructor
    class TestSubClass {
        @FieldId(0)
        int a;
    }

    @Test
    public void testMarshall() {
        TestSubClass scObject = new TestSubClass(4);
        List<Integer> l = Arrays.asList(6,7,8);
        TestClass cObject = new TestClass("string-a", 2, l, scObject);
        TestClass anotherObject = new TestClass("string-a", 2, l, scObject);

        Marshaller.marshall(cObject);
        Marshaller.marshall(anotherObject);
    }

}
