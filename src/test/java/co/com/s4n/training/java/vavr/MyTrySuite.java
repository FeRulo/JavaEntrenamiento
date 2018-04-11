package co.com.s4n.training.java.vavr;

import co.com.s4n.training.java.tallerMap.Premio;
import co.com.s4n.training.java.tallerMap.ServicioPremios;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
import io.vavr.Function1;
import io.vavr.control.Try;
import org.junit.Test;
import static io.vavr.API.*;
import static io.vavr.Predicates.*;
import static io.vavr.Patterns.*;
import static junit.framework.TestCase.assertEquals;
import io.vavr.PartialFunction;
import java.util.ArrayList;
import java.util.stream.Stream;

import java.util.List;
import java.util.function.Consumer;
import static io.vavr.control.Try.failure;
import static org.junit.Assert.assertTrue;

public class MyTrySuite {

    @Test
    public void crearListasParaRepositorio(){
        List<Premio> hamburguesas = ServicioPremios.crearHamburguesas(10);
        List<Premio> cafes = ServicioPremios.crearBonosCafe(20);
        List<Premio> boletas = ServicioPremios.crearBonosCine(5);
        assertEquals("hamburguesa", hamburguesas.get(0).nombre);
        assertEquals("café", cafes.get(0).nombre);
        assertEquals("cine", boletas.get(0).nombre);
    }
    @Test
    public void prueba2(){
        List<Premio> hamburguesas = ServicioPremios.crearHamburguesas(10);
        List<Premio> cafes = ServicioPremios.crearBonosCafe(20);
        List<Premio> boletas = ServicioPremios.crearBonosCine(5);
        Try<String> premio = ServicioPremios.obtenerHamburguesa(hamburguesas)
                .map(p -> p.nombre);
        System.out.println(hamburguesas.size());
        assertEquals(Try.success("hamburguesa"), premio);
    }
    @Test
    public void prueba3(){
        List<Premio> hamburguesas = ServicioPremios.crearHamburguesas(10);
        List<Premio> cafes = ServicioPremios.crearBonosCafe(20);
        List<Premio> boletas = ServicioPremios.crearBonosCine(5);
        Try<String> premio = ServicioPremios.obtenerHamburguesa(hamburguesas)
                .map(p -> p.nombre);
        System.out.println(hamburguesas.size());
        assertEquals(Try.success("hamburguesa"), premio);
    }
}
