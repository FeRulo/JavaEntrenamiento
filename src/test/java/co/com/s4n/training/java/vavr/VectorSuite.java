package co.com.s4n.training.java.vavr;
import io.vavr.collection.Vector;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VectorSuite {

    @Test
    public void firstTest(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        assertEquals(v.get(0), new Integer(1));
        assertEquals(v.size(),7);
    }

    @Test
    public void secondTest(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        assertEquals(v.size(),7);
    }

    @Test
    public void Test2(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        v = v.update(6, 10);
        assertEquals(v.get(6), new Integer(10));
    }

    @Test
    public void Test3(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        v = v.append(8);
        v = v.prepend(0);
        assertEquals(v, Vector.of(0,1,2,3,4,5,6,7,8));
    }
    @Test
    public void Test4(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        v = v.drop(1);
        assertEquals(v, Vector.of(2,3,4,5,6,7));
    }

    @Test
    public void Test5(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        v = v.drop(1);
        assertEquals(v, Vector.of(2,3,4,5,6,7));
    }

    @Test
    public void Test6(){
        Stream<Integer> s = Stream.of(1,2,3,4,5,6,7);
        Vector<Integer> v = Vector.ofAll(s);
        assertEquals(v, Vector.of(1,2,3,4,5,6,7));
    }

    @Test
    public void Test7(){
        Vector<Integer> v = Vector.of(1,2,3,4,5,6,7);
        Vector<Object> v2 = Vector.narrow(v);
        assertEquals(v,v2);
    }
}
