package co.com.s4n.training.java.vavr;

import org.junit.Test;


import io.vavr.PartialFunction;
import io.vavr.control.Option;

import static io.vavr.API.None;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.ArrayList;

import static io.vavr.API.*;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import java.util.List;

import static io.vavr.API.Some;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionSuite {


    /**
     * Un option se puede filtar, y mostrar un some() o un none si no encuentra el resultado
     */
    @Test
    public void testOptionWithFilter() {
        Option<Integer> o = Option(3);

        assertEquals("Does Exist",
                Some(3),
                o.filter(it -> it >= 3));

        assertEquals("Does not Exist the filter",
                None(),
                o.filter(it -> it > 3));
    }

    @Test
    public void t1(){
        Option<Integer> o = Option(3);
        Option<Integer> o2 = o.filter(it -> it > 3);
        Integer i = o2.getOrElse(666);

        assertEquals("Does not Exist the filter",
                new Integer(666), i
                );
    }
    @Test
    public void t2(){
        Option<Integer> o = Option(3);
        Option<Integer> o2 = o.filter(it -> it >= 3);
        Integer i = o2.getOrElse(666);

        assertEquals("Does Exist",
                new Integer(3), i
        );
    }

    /**
     * Se puede hacer pattern matching a un option y comparar entre Some y None.
     */
    private String patternMatchSimple(Option<Integer> number) {
        String result = Match(number).of(
                Case($Some($()),"Existe"),
                Case($None(),"Imaginario")
        );
        return result;
    }

    @Test
    public void testOptionWithPatternMatching() {
        Option<Integer> o1 = Option(1);
        Option<Integer> o2 = None();

        //Comparacion de Some o None()
        assertEquals("Failure match optionList", "Existe", patternMatchSimple(o1));
        assertEquals("Failure match optionList2", "Imaginario", patternMatchSimple(o2));
    }

    /**
     *
     * el metodo peek aplica una funcion lambda o un metodo con el valor de Option cuando esta definido
     * este metodo se usa para efectos colaterales y retorna el mismo Option que lo llamó
     */
    @Test
    public void testPeekMethod(){
        Option<String> defined_option = Option.of("Hello!");
        /* Se debe utilizar una variable mutable para reflejar los efectos colaterales*/
        final List<String> list = new ArrayList<>();
        defined_option.peek(list::add); // the same as defined_option.peek(s -> list.add(s))
        System.out.println(list.size());
        assertEquals("failed - peek did not return the same Option value",
                Option.of("Hello!"),
                defined_option);

        assertEquals("failed - peek did not apply the side effect",
                "Hello!",
                list.get(0));
    }

    @Test
    public void testPeekMethod2(){
        Option<String> defined_option = None();
        /* Se debe utilizar una variable mutable para reflejar los efectos colaterales*/
        final List<String> list = new ArrayList<>();
        defined_option.peek(list::add); // the same as defined_option.peek(s -> list.add(s))
        System.out.println(list.size());
        assertTrue(list.size() == 0);
    }

    /**
     * En este test se prueba la funcionalidad para transformar un Option por medio de Map y flatMap
     */
    @Test
    public void testMap1() {
        Option<String> o1 = Option.of("mi papa");

        Option<String> o2 = o1.map(s -> s + " es bonito");

        assertEquals("Transform Option with Map",
                Option.of("mi mapa es bonito"),
                o2);
    }
    @Test
    public void testMap2() {
        Option<String> o1 = Option.of("mi papa");

        Option<Integer> o2 = o1.map(s -> s.length());

        assertEquals("Transform Option with Map",
                Option.of(7),
                o2);
    }

    /**
     * Un option se puede transformar dada una función
     */
    @Test
    public void testOptionTransform() {
        String textToCount = "Count this text";
        Option<String> text = Option.of(textToCount);
        Option<Integer> count = text.transform(s -> Option.of(s.getOrElse("DEFAULT").length()));

        assertEquals("failure - Option was not transformed",
                Option.of(textToCount.length()),
                count);

        Option<String> hello = Option.of("Hello");
        Tuple2<String, String> result = hello.transform(s -> Tuple.of("OK", s.getOrElse("DEFAULT")));

        assertEquals("failure - Option was not transformed",
                Tuple.of("OK", "Hello"),
                result);
    }

    @Test
    public void testOptionTransform2() {
        Option<Integer> i = Option.of(3);
        String s = i.transform(op -> op.getOrElse(666)+"Hola");
        assertEquals("failure - Option was not transformed",
                "3Hola",
                s);
    }

    @Test
    public void testOptionTransform3() {
        Option<Integer> i = None();
        String s = i.transform(op -> "te amo");
        assertEquals("failure - Option was not transformed",
                "te amo",
                s);
    }

    @Test
    public void testOptionTransform4() {
        Option<Integer> i = None();
        Integer value= i.transform(op -> op.getOrElse(666) + 3);
        assertEquals("failure - Option was not transformed",
                new Integer(669),
                value);
    }

    /**
     * el metodo 'when' permite crear un Some(valor) o None utilizando condicionales booleanos
     */
    @Test
    public void testWhenMethod(){
        Option<String> valid = Option.when(true, "Good!");
        Option<String> invalid = Option.when(false, "Bad!");
        assertEquals("failed - the Option value must contain a Some('Good!')", Some("Good!"), valid);
        assertEquals("failed - the Option value must contein a None because the condtion is false", None(), invalid);
    }

    @Test
    public void testOptionCollect() {
        final PartialFunction<Integer, String> pf = new PartialFunction<Integer, String>() {
            @Override
            public String apply(Integer i) {
                return String.valueOf(i);
            }

            @Override
            public boolean isDefinedAt(Integer i) {
                return i % 2 == 1;
            }
        };
        assertEquals("Failure, it returned Some() it should returned None()", None(),Option.of(2).collect(pf));
        assertEquals("Failure, it returned Some() it should returned None()", None(),Option.<Integer>none().collect(pf));
    }
    /**
     * En este test se prueba la funcionalidad para el manejo de Null en Option con FlatMap
     */
    @Test
    public void testMananagementNull(){
        Option<String> valor = Option.of("pepe");
        Option<String> someN = valor.map(v -> null);

        /* Se valida que devuelve un Some null lo cual podria ocasionar en una Excepcion de JavanullPointerExcepcion*/
        assertEquals("The option someN is Some(null)",
                someN.get(),
                null);

        Option<String> buenUso = someN
                .flatMap(v -> {
                    System.out.println("testManagementNull - Esto se imprime? (flatMap)");
                    return Option.of(v);
                })
                .map(x -> {
                    System.out.println("testManagementNull - Esto se imprime? (map)");
                    return x.toUpperCase() +"Validacion";
                });

        assertEquals("The option is not defined because result is None",
                None(),
                buenUso);
    }

    /**
     * En este test se prueba la funcionalidad para transformar un Option por medio de Map y flatMap
     */
    @Test
    public void testMapAndFlatMapToOption() {
        Option<String> myMap = Option.of("mi mapa");

        Option<String> myResultMapOne = myMap.map(s -> s + " es bonito");

        assertEquals("Transform Option with Map",
                Option.of("mi mapa es bonito"),
                myResultMapOne);

        Option<String> myResultMapTwo = myMap
                .flatMap(s -> Option.of(s + " es bonito"))
                .map(v -> v + " con flat map");


        assertEquals("Transform Option with flatMap",
                Option.of("mi mapa es bonito con flat map"),
                myResultMapTwo);
    }

}
