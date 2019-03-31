package com.czce4013.marshaller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.czce4013.entity.ClientQuery;
import com.czce4013.entity.DateTime;
import com.czce4013.entity.FlightInfo;
import com.czce4013.entity.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MarshallerTest {

    private static final Logger logger = LoggerFactory.getLogger(MarshallerTest.class);

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class TestClass extends Marshallable{

        String a;
        int b;
        List<Integer> c;
        TestSubClass d;
        double e;
        short f;
        float g;
        Date h;
        boolean i;
        TestSubClass j;

        // Will not serialize data in IgnoreField
        @IgnoreField
        // This annotation is just for JSON testing to check the code, not needed in entities
        @JSONField(serialize = false, deserialize = false)
        double noSerialize;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class TestSubClass {
        int a;

    }

    @Test
    public void testMarshall() {
        TestSubClass scObject = new TestSubClass(4);
        List<Integer> l = Arrays.asList(6,7,8);
        TestClass cObject = new TestClass("string-a", 2, null, scObject, 4.32, (short) 12, 0, null, true, null, 1.23);

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

    @Test
    public void testMarshallClientQuery() {
        DateTime dateTime = new DateTime(1999,2,3,4,5);
        FlightInfo flight = new FlightInfo((short) 1,"Changi","KLIA",dateTime,1.23F,(short) 5);
        ClientQuery query = new ClientQuery(1,flight);

        byte[] byteList = query.marshall();
        ClientQuery recast = Marshallable.unmarshall(byteList, ClientQuery.class);

        // Another way to use unmarshall if you don't know the class type
        ClientQuery recast2 = null;
        Marshallable m = Marshallable.unmarshall(byteList);
        if (m instanceof ClientQuery) {
            recast2 = (ClientQuery) m;
        }

        logger.info("original: {}, {}", query, JSON.toJSONString(query));
        logger.info("recast: {}, {}", recast, JSON.toJSONString(recast));
        logger.info("recast2: {}, {}", recast2, JSON.toJSONString(recast2));
        assert JSON.toJSON(query).equals(JSON.toJSON(recast));
        assert JSON.toJSON(query).equals(JSON.toJSON(recast2));

    }

    // @Test
    // public void testMarshallFlightInfo() {
    //     DateTime dateTime = new DateTime(2014,2,3,4,5);
    //     FlightInfo query = new FlightInfo((short) 1,"Changi","KLIA",dateTime,1.23F,(short) 5);

    //     byte[] byteList = query.marshall();
    //     FlightInfo recast = Marshallable.unmarshall(byteList, FlightInfo.class);

    //     // Another way to use unmarshall if you don't know the class type
    //     FlightInfo recast2 = null;
    //     Marshallable m = Marshallable.unmarshall(byteList);
    //     if (m instanceof FlightInfo) {
    //         recast2 = (FlightInfo) m;
    //     }

    //     logger.info("original: {}", query);
    //     logger.info("recast: {}", recast);
    //     logger.info("recast2: {}", recast2);
    //     assert JSON.toJSON(query).equals(JSON.toJSON(recast));
    //     assert JSON.toJSON(query).equals(JSON.toJSON(recast2));

    // }

    @Test
    public void testMarshallServerResponse() {
        DateTime dateTime = new DateTime(1993,2,3,4,5);
        DateTime dateTime2 = new DateTime(2015,8,7,6,5);
        FlightInfo query = new FlightInfo((short) 1,"Changi","KLIA",dateTime,1.23F,(short) 5);
        FlightInfo query2 = new FlightInfo((short) 2,"Bangkok","Tokyo",dateTime2,2.34F,(short) 10);

        FlightInfo[] arr ={query, query2};
        ServerResponse queryArray = new ServerResponse(0, 200,Arrays.asList(arr));
        byte[] byteList = queryArray.marshall();
        ServerResponse recast = Marshallable.unmarshall(byteList, ServerResponse.class);

        // Another way to use unmarshall if you don't know the class type
        ServerResponse recast2 = null;
        Marshallable m = Marshallable.unmarshall(byteList);
        if (m instanceof ServerResponse) {
            recast2 = (ServerResponse) m;
        }

        logger.info("original: {}", queryArray);
        logger.info("recast: {}", recast);
        logger.info("recast2: {}", recast2);
        assert JSON.toJSON(queryArray).equals(JSON.toJSON(recast));
        assert JSON.toJSON(queryArray).equals(JSON.toJSON(recast2));

    }

}
