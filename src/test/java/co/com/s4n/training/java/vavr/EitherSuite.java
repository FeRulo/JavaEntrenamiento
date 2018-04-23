package co.com.s4n.training.java.vavr;

import co.com.s4n.training.java.tallerEither.A;
import co.com.s4n.training.java.tallerEither.B;
import io.vavr.Function1;
import io.vavr.control.Either;
import org.junit.Test;

import static io.vavr.API.Left;
import static io.vavr.API.None;
import static io.vavr.API.Right;

import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.io.Serializable;
import static org.junit.Assert.assertArrayEquals;
import io.vavr.control.Option;
import io.vavr.control.Try;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static io.vavr.API.*;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;
import static org.junit.Assert.*;

public class EitherSuite {

    /**
     * Se valida la funcion swap con un Either right y left en la cual se aplica swap para el cambio de tipo Either
     */
    @Test
    public void swapToEitherLeft() {

        Either<Integer,String> myEitherL = Either.left(14);
        assertTrue("Valide swap before in Either Left", myEitherL.isLeft());

    }
    @Test(expected = NoSuchElementException.class)
    public void EitherGet() {
        Either<Integer, String> myEitherL = Either.left(14);
        System.out.println(myEitherL.get() + "");
    }
    @Test(expected = NoSuchElementException.class)
    public void EitherGetLeft() {
        Either<Integer,String> myEitherR = Either.right("String");
        System.out.println(myEitherR.getLeft() + "");
    }
    @Test
    public void swapToEitherRight() {
        Either<Integer,String> myEitherR = Either.right("String");
        System.out.println(myEitherR.get());
        assertTrue("Valide swap before in Either Right", myEitherR.isRight());
    }
    @Test
    public void swapToEitherSwap() {
        Either<Integer,String> myEitherR = Either.right("String");
        Either<Integer,String> myEitherL = Either.left(14);
        assertTrue("Valide swap after in Either Right", myEitherR.swap().isLeft());
        assertTrue("Valide swap after in Either Right", myEitherL.swap().isRight());
    }

    /**
     * Los Either se definen porque tienen un manejo por convencion derecha el valor correcto e
     * izquierda si fue incorrecto, se usan las projecciones para saber con cual lado se operará.
     * Es decir si los Either son correctos es porque tienen una valor en la derecha y se sumara un valor x,
     * sino el lado izquierdo sera un mensaje que diga either incorrecto.
     */
    @Test
    public void testProjectionRight(){
        Either<Integer,Integer> e1 = Either.right(5);


        //El Either por defecto cuando se usa el map opera con el lado derecho.
        assertEquals("Failure - Right projection", Right(10), e1.map(it -> it + 5));
    }
    @Test
    public void testProjectionLeft(){

        Either<Integer,Integer> e2 = Either.left(5);

        //El Either para operar el lado izquierdo se debe usar un mapLeft.
        assertEquals("Failure - Left Projection", Left(10), e2.mapLeft(it -> it + 5));
    }
    @Test
    public void testProjectionLeftMap(){

        Either<Integer,Integer> e2 = Either.left(5);
        Either<Integer, Object> test = Either.left(10);
        Either<Integer, Integer> test1 = e2.mapLeft(it -> it + 5);

        //El Either para operar el lado izquierdo se debe usar un mapLeft.
        assertEquals("Failure - Left Projection", test, test1);
    }

    /**
     * El map por defecto operara el lado derecho, si el either tiene su informacion en el lado izquierdo
     * este no se modificara
     */
    @Test
    public void testEitherMap() {
        Either<String,Double> value = Either.right( 2.0 / 3);

        assertEquals("Failure - Map in Right",
                Right(4.0),
                value.map(aDouble -> aDouble * 6));

        Either<String,Double> value2 = Either.left("Left side");

        assertEquals("Failure - the Either is not left",
                Left("Left side"),
                value2.map(aDouble -> aDouble * 6));

    }

    /**
     * El flatmap por defecto operara el lado derecho, si el either tiene su informacion en el lado izquierdo
     * este no se modificara
     */
    @Test
    public void testEitherFlatMap() {

        Either<String,Double> e1 = Either.right( 2.0 / 3);

        assertEquals("Failure - flatMap in Right",
                Right(4.0),
                e1.flatMap(aDouble -> Right(aDouble * 6)));

        Either<String,Double> e2 = Either.left("Left side");

        assertEquals("Failure - the Either is not left",
                Left("Left side"),
                e2.flatMap(aDouble -> Right(aDouble * 6)));

    }


    public Either<String,Integer> dividir(Integer a, Integer b) {
        return (b==0)?Left("No se puede dividir por cero"):Right(a/b);
    }

    public Either<String,Integer> dividir1(Integer a, Integer b) {
        return Either.right(b)
                .filter(i->i!=0)
                .getOrElse(Either.left("No se puede dividir por cero"))
                .mapLeft(s->""+s)
                .map(i-> a/i);
    }
    @Test
    public void testForEach() {
        Either<String,Double> e = Either.right( 2.0 / 3);
        e.forEach(d-> System.out.println(d));
        Either<String,Double> e2 = Either.left("hola");
        e2.forEach(d-> System.out.println(d));
    }
    @Test
    public void testForEach2() {
        Either<String,Double> e2 = Either.left("hola");
        e2.forEach(d-> System.out.println(d));
    }
    @Test
    public void testEitherFilterdividircero() {

        Either<String,Integer> value = Either.right(10);
        Either<String,Integer> value2 = Either.right(0);

        Either<String, Integer> result = value
                .flatMap(i -> value2
                .flatMap(i2 -> dividir1(i, i2)));

        System.out.println(result);

        assertEquals(Left("No se puede dividir por cero"),result);
    }
    @Test
    public void testEitherFilterdividir() {

        Either<String,Integer> value = Either.right(10);
        Either<String,Integer> value2 = Either.right(5);

        Either<String, Integer> result = value.flatMap(i -> value2.flatMap(i2 -> dividir(i, i2)));

        System.out.println(result);

        assertEquals(Right(2),result);
    }
    /**
     * Un Either puede ser filtrado, y en el predicado se pone la condicion
     */
    @Test
    public void testEitherFilter() {

        Either<String,Integer> value = Either.right(7);

        assertEquals("value is even",
                None(),
                value.filter(it -> it % 2 == 0));
    }
    @Test
    public void testEitherFilterSome() {

        Either<String,Integer> value = Either.right(6);

        assertEquals("value is even",
                Option.of(Right(6)),
                value.filter(it -> it % 2 == 0));
    }

    /**
     * Si el predicado del filter tiene un null, el void lanzara un Nullpointerexception
     */
    @Test(expected = NullPointerException.class)
    public void testEitherFilter2() {
        Either<String,Integer> value = Either.right(7);
        value.filter(null);
    }

    /**
     * La funcion bimap me permite realizar map a los Either en su derecha o izquierda
     * esto dependera de en cual lado tiene informacion
     */
    @Test
    public void testEitherBiMap() {
        Function1<String,String> left = (Function1<String , String>) string -> "this is the left";
        Function1<Integer,Integer> right = (Function1<Integer,Integer>) integer -> integer + 15;

        Function1<Either,Either> biMap = (Function1<Either, Either>) either ->
                either.bimap(left,right);

        Either<String,Integer> value = Either.right(5);
        Either<String,Integer> value2 = Either.left("this is some");

        assertEquals("Failure map in right", Either.right(20),biMap.apply(value));
        assertEquals("Failure map in left", Either.left("this is the left"),biMap.apply(value2));
    }

    /**
     * Se valida la funcion orElseRun, la cual ejecuta una accion
     * si el either es either.left.
     */
    @Test
    public void testOrElseRun() {
        Either<Integer,String> myEitherR = Either.right("String");
        Either<Integer,String> myEitherL = Either.left(14);
        final String[] result = {"let's dance! "};
        Consumer<Object> addIfTrue = element -> {
            result[0] += element;
        };
        myEitherR.orElseRun(addIfTrue);
        assertEquals("Valide swap before in Either Right",
                "let's dance! ", result[0]);
        myEitherL.orElseRun(addIfTrue);
        assertEquals("Valide swap before in Either Right",
                "let's dance! 14", result[0]);


    }

    /**
     * Se valida la funcion peek y peekleft segun la proyeccion del Either ya sea right o left
     */
    @Test
    public void peekToEither() {

        final String[] valor = {"default"};
        Either<String,String> myEitherR = Either.right("123456");
        Either<String,String> myEitherL = Either.left("1234567");

        Consumer<String> myConsumer = element -> {
            if(element.length()>6){
                valor[0] = "foo";
            }
            else {
                valor[0] = "bar";
            }
        };

        myEitherL.peek(myConsumer);
        assertEquals("Validete Either with peek","default", valor[0]);

        myEitherR.peek(myConsumer);
        assertEquals("Validete Either with peek","bar", valor[0]);

        myEitherL.peekLeft(myConsumer);
        assertEquals("Validete Either with peek","foo", valor[0]);
    }

    /**
     * Uso de pattern matching para capturar un Either.Left
     */
    @Test
    public void testPatternMatchingLeftSide(){
        Option<String> none = None();

        //toEither method transform Option<String> to String
        Either<String, String> left = none.toEither(() -> "Left from None value");

        String result = Match(left).of(
                Case($Left($()), msg -> msg),
                Case($(), "Not found")
        );
        assertEquals("Failure - Pattern of either left doesn't catch the left projection", left.getLeft(), result);
    }

    /**
     * Uso de pattern matching para capturar un Either.Right
     */
    @Test
    public void testPatternMatchingRightSide(){
        Option<String> value = Option.of("Right value rules!");
        //toEither method transform Option<String> to String
        Either<String, String> right = value.toEither(() -> "Left from None value");
        String result = Match(right).of(
                Case($Right($()), msg -> msg),
                Case($(), "Not found")
        );
        assertEquals("Failure - Pattern of either right doesn't catch the left projection", right.getOrElse(""), result);
    }

    /**
     * Uso de narrow para duplicar objetos Either.
     */
    @Test
    public void testNarrow(){
        Either<Integer, String> either = Try.of(()-> "0").toEither(0);
        Either<Object, Object> copy = Either.narrow(either);
        assertEquals("Failure - the result of narrows must be equals to the source",either,copy);
        assertSame("Failure - Although the narrowed and the source have a different type specification, they must be the same object",
                either,
                copy);
    }

    public class C extends A { }
    public class D extends B { }
    @Test
    public void testNarrowclass(){

        Either<C, D> either=null;

        Either<A, B> tes = Either.narrow(either);

        System.out.println(tes);

    }


    /**
     * Las proyecciones de Either se pueden transformar con fold.
     * Retorna por defecto la proyección derecha si el Either tiene dicha proyección.
     */
    @Test
    public void testFoldRightsimple(){
        Either<String,Integer> e1=Either.right(1);
        Serializable testright = e1.fold(i -> i, i -> i + 1);

        System.out.println(testright);

        assertEquals(2,testright);
    }

    @Test
    public void testFoldLeftsimple(){
        Either<String,Integer> e1=Either.left("Test");
        Serializable testleft = e1.fold(i -> i + " fold", i -> i + 1);

        System.out.println(testleft);

        assertEquals("Test fold",testleft);
    }

    @Test
    public void testFoldRightSimple(){


        String[] actual = transform("text_to_transform", true).fold(l -> l.split("_"), r -> r.split("_"));
        String[] expected = {"TEXT", "TO", "TRANSFORM"};
        assertArrayEquals("The arrays were not the same", expected, actual);
    }

    /**
     * Las proyecciones de Either se pueden transformar con fold.
     * Retorna por defecto la proyección derecha si el Either tiene dicha proyección; de lo
     * contrario, retorna la proyección izquierda
     */
    @Test
    public void testFoldLeft(){
        String[] actual = transform("text_to_transform", false).fold(l -> l.split("_"), r -> r.split("_"));
        String[] expected = {"text", "to", "transform"};
        assertArrayEquals("The arrays were not the same", expected, actual);
    }

    /**
     * Método utilitario para las pruebas de fold
     * @param s String a transformar
     * @param hasRightProjection si se le debe asignar valor en la derecha o no
     * @return Either<String, String>
     */
    private Either<String, String> transform(String s, boolean hasRightProjection){
        Either<String, String> either = Either.left(s.toLowerCase());
        if (hasRightProjection) either = Either.right(s.toUpperCase());
        return either;
    }

    /**
     * Aunque la documentación de fold either dice que el tipado de los mapper para cada proyección
     * debe ser igual, aún así, el compilador permite crear los mapper con tipos diferentes.
     */
    @Test
    public void testFoldWithTwoMappers(){
        Serializable rightProjection = transform("text_to_transform", true).fold(l -> l.split("_"), r -> r.length());
        assertEquals("Right projection fold was nos successful","17", rightProjection.toString());
        Serializable leftProjection = transform("text_to_transform", false).fold(l -> l.split("_"), r -> r.length());
        String[] expected = {"text", "to", "transform"};
        assertArrayEquals("Left projection fold was nos successful", expected, (String[])leftProjection);
    }
}