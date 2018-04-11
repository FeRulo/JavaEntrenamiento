package co.com.s4n.training.java.vavr;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.function.Function;

import static io.vavr.API.*;
import static org.junit.Assert.assertEquals;

public class ForComprenhensionSuite {

    /*
     * Pruebas de encadenamiento con Try
     * */

    public Try<Integer> sumar(Integer d){
        System.out.println("en sumar");
        return Try.of(()->d+1);
    }
    public Try<Integer> dividir(Integer num, Integer den){
        System.out.println("en dividir");
        return Try.of(()->num/den);
    }

    @Test
    public void testEncadenamientoTry1(){
        Try<Integer> t =
                For(dividir(2,1), i->
                For(dividir(3,1), i2 ->
                For(dividir(6,3), i3 ->
                sumar(i+i2+i3)
                ))).toTry();
        assertEquals( t, Success(8));
    }
    @Test
    public void testEncadenamientoTry2() {
        Try<Integer> o =
                For(dividir(2, 1), i ->
                For(dividir(3, 0), i2 ->
                For(dividir(6, 3), i3 ->
                sumar(i + i2 + i3)
                ))).toTry();
        assertEquals(o, None());
    }
    /*
    * Pruebas de Option
    * */

    public Option<Integer> esPar(Integer a){
        System.out.println("validando");
        return Option.of(a).filter(i-> i % 2 == 0);
    }

    @Test
    public void testEncadenamientoOption1(){
        Option<Integer> o =
                For(esPar(2), i->
                For(esPar(4), i2 ->
                For(esPar(6), i3 -> Option.of(i+i2+i3)
            ))).toOption();
        assertEquals( o, Some(12));
    }

    @Test
    public void testEncadenamientoOption2(){
        Option<Integer> o =
                For(esPar(2), i->
                For(esPar(3), i2 ->
                For(esPar(6), i3 -> Option.of(i+i2+i3)
                ))).toOption();
        assertEquals( o, None());
    }
}
