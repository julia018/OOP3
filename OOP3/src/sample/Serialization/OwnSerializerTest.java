package sample.Serialization;

import org.junit.Test;
import sample.buildings.*;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;
import static sample.Controller.createStadium;

public class OwnSerializerTest {
    private final OwnSerializer TestOwnSerializer = new OwnSerializer();

    @Test
    public void testSerializeObjectForObjectClass() throws Exception {
        football_stadium FS = createStadium();
        String objInString = TestOwnSerializer.serializeObject(FS);
        assertEquals(TestOwnSerializer.deserializeObject(objInString).getClass(), football_stadium.class);
    }


    @Test
    public void testCreateObject() throws Exception {
        String nameForClass = "sample.buildings.tennis_cort";
        assertEquals(TestOwnSerializer.createObject(nameForClass).getClass(), tennis_cort.class);
    }

    @Test
    public void testDeserializeObjectForClass() throws Exception {
        //String obj = "{sample.buildings.football_stadium;name:Лужники;light_type:synthetic;fl_amount:10;capacity:200000;location:Россия, Москва;parking:100000;vip:false;pl_gate:{sample.buildings.gate;mobility:true;width:100.0;height:50.0;}pl_field:{sample.buildings.field;fence:true;gr_type:natural;}team:Спартак;}";
        String serealizedGates = "{sample.buildings.gate;mobility:true;width:100.0;height:50.0;}";
        assertEquals(TestOwnSerializer.deserializeObject(serealizedGates).getClass(), gate.class);
    }
}