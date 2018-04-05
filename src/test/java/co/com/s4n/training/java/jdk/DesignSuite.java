package co.com.s4n.training.java.jdk;


import co.com.s4n.training.java.Concatenador;
import co.com.s4n.training.java.ServiceClient;
import java.util.*;


import static org.junit.Assert.*;
import org.junit.Test;

public class DesignSuite{
    @Test
    public void smokeTest() {
        assertTrue(true);
    }

    @Test
    public void pruebaConcatenar(){
        //System.out.println(Concatenador.concat("hola", "mundo"));
        assertEquals("holamundo", Concatenador.concat("hola", "mundo"));
        assertEquals("hola mundo", Concatenador.concat2("hola", "mundo"));
    }

    @Test
    public void probandoMockito(){
        //You can mock concrete classes, not just interfaces
        List mockedList = createMock(List.class);
        ServiceClient mockedClient = mock(ServiceClient.class);
        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        when(mockedClient.useMethod(new Concatenador(), "hola", "mundo")).thenReturn("hola mundo");
        System.out.println(mockedList.get(0));
        //System.out.println(mockedClient.useMethod(new Concatenador(), "hola", "mundo"));

    }
}
