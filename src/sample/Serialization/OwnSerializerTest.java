package sample.Serialization;
import org.junit.Test;
import sample.buildings.*;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class OwnSerializerTest {
    private final OwnSerializer TestOwnSerializer = new OwnSerializer();
    private final pool testPool = new pool();
    private final track  testTrack = new track();

    @Test
    public void testSerializeObjectForNull() throws Exception{
        assertEquals(TestOwnSerializer.serializeObject(null), "{null};");
    }

    @Test
    public void testSerializeInnerObject() throws Exception{
        assertEquals(TestOwnSerializer.serializeObject(null), "{null};");
    }

    @Test
    public void testSerializeObjectForObjectClass() throws Exception{
        //Поле
        field myField = new field();
        myField.setgr_type("natural");
        myField.setfence(true);

        //Ворота
        gate Gates = new gate();
        Gates.setheight("50");
        Gates.setwidth("100");
        Gates.setmobility(true);

        //Стадион
        football_stadium FS = new football_stadium();
        FS.setpl_field(myField);
        FS.setpl_gate(Gates);
        FS.setname("Лужники");
        FS.setteam("Спартак");
        FS.setcapacity("200000");
        FS.setfl_amount("10");
        FS.setvip(false);
        FS.setlight_type("synthetic");
        FS.setlocation("Россия, Москва");
        FS.setparking("100000");
        String objInString = TestOwnSerializer.serializeObject(FS);
        assertEquals(TestOwnSerializer.deserializeObject(objInString).getClass(), football_stadium.class);
    }


    @Test
    public void testCreateObject() {
        String nameForClass = "sample.buildings.tennis_cort";
        try {
            assertEquals(TestOwnSerializer.createObject(nameForClass).getClass(), tennis_cort.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeObjectForClass() throws Exception{
        //String obj = "{sample.buildings.football_stadium;name:Лужники;light_type:synthetic;fl_amount:10;capacity:200000;location:Россия, Москва;parking:100000;vip:false;pl_gate:{sample.buildings.gate;mobility:true;width:100.0;height:50.0;}pl_field:{sample.buildings.field;fence:true;gr_type:natural;}team:Спартак;}";
        String serealizedGates = "{sample.buildings.gate;mobility:true;width:100.0;height:50.0;}";
        assertEquals(TestOwnSerializer.deserializeObject(serealizedGates).getClass(), gate.class);

    }
}