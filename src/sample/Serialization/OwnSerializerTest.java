package sample.Serialization;

import org.junit.Test;
import sample.buildings.*;

import static org.junit.Assert.*;

public class OwnSerializerTest {
    private final OwnSerializer TestOwnSerializer = new OwnSerializer();
    private final pool testPool = new pool();
    private final track  testTrack = new track();

    @Test
    public void testSerializeForNull() throws Exception{
        //assertEquals(TestOwnSerializer.serialize(null), "{null}");
    }

    /*@Test
    public void testSerializeForObjectClass() throws Exception{
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
        //String objInString = TestOwnSerializer.serialize(FS, football_stadium.class);

        assertEquals(TestOwnSerializer.serialize(Gates), "{sample}; ");
        // assertEquals(TestOwnSerializer.deserialize(objInString).getClass(), football_stadium.class);
    }*/


    @Test
    public void write() {
    }

    @Test
    public void testDeserialize() throws Exception{
        String obj = "{sample.buildings.football_stadium;name:Лужники;light_type:synthetic;fl_amount:10;capacity:200000;location:Россия, Москва;parking:100000;vip:false;pl_gate:{sample.buildings.gate;mobility:true;width:100.0;height:50.0;}pl_field:{sample.buildings.field;fence:true;gr_type:natural;}team:Спартак;}";
        String gat = "{sample.buildings.gate;mobility:true;width:100.0;height:50.0;}";
        //System.out.println(((football_stadium)TestOwnSerializer.deserialize(obj)).getpl_field().toString());
        //System.out.println(TestOwnSerializer.serialize(TestOwnSerializer.deserialize(obj), obj.getClass()));
        //gate myfs = (gate) TestOwnSerializer.deserialize(obj);
        //System.out.println(TestOwnSerializer.serialize(gat));
        //Class<? extends sport_fac> objectt = (Class<? extends sport_fac>) TestOwnSerializer.deserialize(obj);
        //System.out.println(TestOwnSerializer.serialize(TestOwnSerializer.deserialize(obj)));
        System.out.println(((football_stadium)TestOwnSerializer.deserialize(obj)).getvip());
        assertEquals(TestOwnSerializer.deserialize(obj).getClass(), football_stadium.class);

    }
}