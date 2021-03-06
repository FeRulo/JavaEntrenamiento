package co.com.s4n.training.java.vavr;

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
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import java.util.List;
import java.util.function.Consumer;
import static io.vavr.control.Try.failure;
import static org.junit.Assert.assertTrue;

public class TrySuite {

    /**
     *En este test se validara cuando un Try por medio de la ejecucion de una funcion
     * devuelve Success en caso de exito o failure con la encapsulacion del error
     */
    @Test
    public void testTrytoSuccesAndFailure(){
        Try<Integer> myTrySuccess = Try.of(() -> 15 / 5 );
        Try<Integer> myTryFailure = Try.of(() -> 15 / 0 );

        assertEquals("failed - the values is a Failure",
                Success(3),
                myTrySuccess);

        assertTrue("failed - the values is a Failure",
                myTryFailure.isFailure());
    }

    private String patternMyTry(Try<Integer> myTry) {
        return Match(myTry).of(
                Case($Success($()),"Este Try es exitoso"),
                Case($Failure($()),"Este Try es fallido"));
    }

    /**
     * Validar pattern Matching a un Try validado entre Succes y failure.
     */
    @Test
    public void testTryToPatternMatching() {

        Try<Integer> myTrySuccess = Try.of(() -> 15 / 5 );
        Try<Integer> myTryFailure = Try.of(() -> 15 / 0 );

        assertEquals("Failure match optionList",
                "Este Try es exitoso",
                patternMyTry(myTrySuccess));

        assertEquals("Failure match optionList2",
                "Este Try es fallido",
                patternMyTry(myTryFailure));
    }

    private Try<Integer> recoverMyTry(Integer a, Integer b) {
        return Try.of(() -> a / b).recover(x -> Match(x).of(
                Case($(instanceOf(Exception.class)), -1)));
    }

    /**
     * Validar el uso de recover para retornar un Integer por defecto en caso de error o el valor transformado acorde a la funcion
     */
    @Test
    public void testTryToRecover() {

        Try<Integer> myRecoverSuccess = recoverMyTry(15, 5);
        Try<Integer> myRecoverFailure = recoverMyTry(15, 0);

        assertEquals("Failed - Error nor controlled",
                Success(3),
                myRecoverSuccess);

        assertEquals("Failed - Error nor controlled",
                Success(-1),
                myRecoverFailure);
    }


    /**
     * La funcionalidad AndThen usa el parametro de salida de la anterior funcion cómo
     * parametro de entrada de la siguiente función.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSuccessAndThen() {
        Try<Integer> actual = Try.of(() -> new ArrayList<Integer>())
                .andThen(arr -> arr.add(10))
                .andThen(arr -> arr.add(30))
                .andThen(arr -> arr.add(20))
                .map(arr -> arr.get(10));
        actual.get();
    }


    /**
     * La funcionalidad transform permite aplicar una modificación
     * sobre la salida de la función.
     */
    @Test
    public void testSuccessTransform() {
        Try<Integer> number = Try.of(() -> 5);
        String transform = number.transform(self -> self.get() + " example of text");

        assertEquals("Failure - it should transform the number to text",
                "5 example of text",
                transform);
    }

    /**
     * La funcionalidad transform va a generar error sobre un try con error.
     */
    @Test(expected = Error.class)
    public void testFailTransformWhen() {
        Try<Integer> error = Try.of(() -> {throw new Error("Error de Jhonatan"); });
        System.out.println(error);
        String str = error.transform(self -> self.get() + " example of text");
    }

    /**
     * flatMap permite mappear un try y anidar varios mapeos sin crear multiples Try encadenados
     * , es decir, busca evitar la creacion de variables tipo Try[Try[Ty[...]]] al encadenar varios mapeos sobre success
     */
    @Test
    public void testFlatMapOnSuccess() {
        CheckedFunction2<Integer, Integer, Integer> divide = (dividend, divisor) -> dividend / divisor;

        Try<Integer> result = Try.of(() -> divide.apply(3, 1));
        System.out.println(result);
        Function1<Try<Integer>,Try<Integer>> mapper = try_var -> {
            System.out.println("operando");
            return try_var
                    .flatMap(i -> Try.of(() -> i * 10))
                    .flatMap(i_10 -> Try.of(() -> i_10 * 10));
        };

        Try<Integer> success_example = mapper.apply(result);

        assertEquals("failed - flatMap on success try case wasn't working as expected",
                Success(300),
                success_example);
    }

    /**
     * flatMap permite encadenar Try aunque alguno de ellos falle
     */
    @Test
    public void testFlatMap() {
        CheckedFunction2<Integer, Integer, Integer> divide = (dividend, divisor) -> dividend / divisor;
        Function1<Try<Integer>,Try<Integer>> mapper = try_var -> {
            System.out.println("operando");
            return try_var
                    .flatMap(i -> {
                        System.out.println("operando2");
                        return Try.of(() ->  i * 10);})
                    .flatMap(i_10 -> Try.of(() -> i_10 * 10));
        };

        Try<Integer> exception = Try.of(() -> divide.apply(3,0));
        Try<Integer> fail_example = mapper.apply(exception);
        System.out.println(fail_example.toString());
        assertEquals("failed - flatMap on failed try case wasn't working as expected",
                failure(new ArithmeticException("/ by zero")).toString(),
                fail_example.toString());
    }

    /**
     * Un try se puede encadenar con funciones que lancen excepciones con AndThenTry
     */
    @Test
    public void testAndThenTry() {
        CheckedFunction2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        CheckedFunction2<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        Try<Integer> tryToDivide = Try.of(() -> divide.apply(70, 2));

        Try<Integer> tryToMultiply = tryToDivide.andThenTry(i -> multiply.apply(i, 2));
        assertTrue("failure - The chaining of tries failed", tryToMultiply.isSuccess());
        tryToDivide = Try.of(() -> divide.apply(70, 0));
        tryToMultiply = tryToDivide.andThenTry(i -> multiply.apply(i, 2));
        assertTrue("failure - The chaining of tries succeded", tryToMultiply.isFailure());
    }

    /**
     * En caso de que se use andThen, se debe manejar la excepción con try-catch
     * AndThen no maneja checked exceptions
     */
    @Test
    public void testAndThenWithChecked() {
        CheckedFunction2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        CheckedFunction2<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        Try<Integer> tryToDivide = Try.of(() -> divide.apply(70, 2));
        Try<Integer> tryToMultiply = tryToDivide.andThen(i -> {
            try {
                multiply.apply(i, 2);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        assertTrue("failure - The chaining of tries failed", tryToMultiply.isSuccess());
    }
    @Test
    public void testAndThenWithChecked2() {
        CheckedFunction2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        CheckedFunction2<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        Try<Integer> tryToDivide = Try.of(() -> divide.apply(70, 0));
        Try<Integer> tryToMultiply = tryToDivide.andThen(i -> {
            try {
                System.out.println("dentro de try");
                multiply.apply(i, 2);
            } catch (Throwable throwable) {
                System.out.println("dentro de catch");
                throwable.printStackTrace();
            }
        });
        assertTrue("failure - The chaining of tries failed", tryToDivide.isFailure());
    }

    /**
     * Collect permite aplicar una funcion parcial a un Try
     */
    @Test
    public void testCollect(){
        PartialFunction<Double, Double> square_root =  new PartialFunction<Double, Double>() {
            @Override
            public Double apply(Double i) {
                return Math.sqrt(i);
            }

            @Override
            public boolean isDefinedAt(Double i) {
                return i >= 0;
            }

        };
        Try<Double> valid = Try.of(()->16D);
        assertEquals("failed - Partial function was not applied correctly for a value of its domain",
                Try.of(() -> 4D),
                valid.collect(square_root));

        Try<Double> invalid = Try.of(() -> -25.0);
        assertTrue("failed - Partial function was not applied correctly for a value that doesn't belong to its domain",
                invalid.collect(square_root).isFailure());
    }

    /**
     * withResource es el metodo seguro para crear un try en base a una instancia de una clase que implemente la interfaz Autocloseable
     */
    @Test(expected = IllegalStateException.class)
    public void testTryWithResources(){
        Stream<Integer> stream = Stream.of(1,2,3,4,5,6,7,8,9,10);
        Try<String> try_stream = Try.withResources(() -> stream).of(s -> s.toString());
        assertTrue("Failure - try was not successfully created", try_stream.isSuccess());
        stream.count();
    }

    public Try<Double> dividir(Double numerador, Double denominador){
        return Try.of(()->numerador / denominador);
    }
    @Test
    public void testInception(){
        Try<Double> t1 = dividir(2D,1D);
        Try<Double> t2 = dividir(3D,1D);
        Try<Double> t3 = dividir(6D,3D);
        Try<Double> tf = t1
                .flatMap(d -> t2
                .flatMap(d2 -> t3
                .flatMap(d3 -> Try.of(()-> d + d2 + d3))));
        assertEquals(Success(7D), tf);
    }

    @Test
    public void testInception2(){
        Try<Double> tf = dividir(2D,1D)
                .flatMap(d -> dividir(3D, 1D)
                .flatMap(d2 -> dividir(6D,3D)
                .flatMap(d3 -> Try.of(()->d + d2 + d3))));
        assertEquals(Success(7D), tf);
    }

    public Try<Double> sumar(Double d){
        return Try(()->d+1);
    }

    @Test
    public void testKarateKid(){
        Try<Double> tf = dividir(2D,1D)
                .flatMap(d -> dividir(3D, 1D)
                .flatMap(d2 -> dividir(6D,3D)
                .flatMap(d3 -> sumar(d + d2 + d3))));
        assertEquals(Success(8D), tf);
    }

    public Try<Integer> sumarLog(Integer d){
        System.out.println("en sumar");
        return Try.of(()->d+1);
    }
    public Try<Integer> dividirLog(Integer num, Integer den){
        System.out.println("en dividir");
        return Try.of(()->num/den);
    }

    @Test
    public void testKarateKid2(){
        Try<Integer> tf = dividirLog(2,1)
                .flatMap(d -> dividirLog(3, 0)
                .flatMap(d2 -> dividirLog(6,3)
                .flatMap(d3 -> sumarLog(d + d2 + d3))));
        System.out.println(tf);
        assertTrue(tf.isFailure());
    }

    /**
     * La funcionalidad peek permite realizar una acción dependiendo de
     * una condición.
     */
    @Test
    public void testErrorPeek() {
        final List<String> tmp = new ArrayList<>();
        Consumer<Object> addIfTrue = element -> {
            if (element.toString().contains("add")) {
                tmp.add("element");
            }
        };
        Try.of(() -> {throw new Error("Error 1");})
                .peek(addIfTrue);
        assertEquals("Failure - it should not add the element",
                true,
                tmp.isEmpty());
    }

    /**
     * La funcionalidad peek permite realizar una acción dependiendo de
     * una condición.
     */
    @Test
    public void testSuccessPeek() {
        final List<String> tmp = new ArrayList<>();
        Consumer<Object> addIfTrue = element -> {
            if (element.toString().contains("add")) {
                tmp.add("element");
            }
        };
        Try.of(() -> "add")
                .peek(addIfTrue);
        assertEquals("Failure - it should add the element",
                "element",
                tmp.get(0));
    }

    /**
     * Validar el uso de Map para transformar un Try de String en otro String con mas informacion
     */

    @Test
    public void testMapToTrySuccess() {
        Try<String> myRecoverSuccess =  Try.of(()-> ", Cool" ).map(x -> "This Try is good" + x);
        assertEquals("Failed - Error nor controlled", Success("This Try is good, Cool"), myRecoverSuccess);
    }

    /**
     * Validar el uso de Map para transformar un Try de String en otro String con mas informacion
     */

    @Test
    public void testMapToTryFailure() {
        Try<Integer> myRecoverSuccessOne =  Try.of(()-> 3 ).map(x -> x/0);
        assertTrue("Failed - Error nor controlled",myRecoverSuccessOne.isFailure());
    }

    /**
     * Filtrar un Try de tipo entero con filter y filterTry devolviendo Success si es multiplo de 3 o Failure si No lo es
     */
    @Test
    public void testFilterToTry() {
        Try<Integer> myFilterSuccess =  Try.of(()-> 12 ).filter(x -> x%3==0);
        Try<Integer> myFilterFailure =  Try.of(()-> 12 ).filter(x -> x%3/0==0);
        System.out.println(myFilterSuccess);
        assertTrue("Failed - Error nor controlled", myFilterFailure.isFailure());
        assertEquals("Failed - Error nor controlled", Success(12), myFilterSuccess);
    }
    @Test
    public void testFilterToTry2() {
        Try<Integer> myFilterSuccess =  Try.of(()-> 13 ).filter(x -> x%3==0);
        Try<Integer> myFilterFailure =  Try.of(()-> 12 ).filter(x -> x%3/0==0);
        System.out.println(myFilterSuccess);
        assertTrue("Failed - Error nor controlled", myFilterFailure.isFailure());
        assertEquals("Failed - Error nor controlled",
                Try.failure( new NoSuchElementException("Predicate does not hold for 13")).toString(),
                myFilterSuccess.toString());
    }

    /**
     * Filtrar un Try de tipo entero con filter y filterTry devolviendo Success si es multiplo de 3 o Failure si No lo es
     */
    @Test
    public void testFilterTryToTry() {
        CheckedFunction2<Integer,Integer,Integer> my = ((a,b) -> a /b);
        Try<Integer> myFilterTrySuccess =  Try.of(()-> 15 ).filterTry(x -> (x + my.apply(6,2))%3==0);
        Try<Integer> myFilterTryFailure =  Try.of(()-> 15 ).filterTry(x -> (x + my.apply(6,0))%3==0);
        assertTrue("Failed - Error nor controlled", myFilterTryFailure.isFailure());
        assertEquals("Failed - Error nor controlled", Success(15), myFilterTrySuccess);
    }




    /**
     *  El recover with debe retornar un Try de el error que recupere.
     */
    @Test
    public void testTryAndRecoverWith() {
        Try<Integer> aTry = Try.of(() -> 2/0).recoverWith(ArithmeticException.class,Try.of(() ->  2));
        Try<Integer> aTry2 = Try.of(() -> 2/0).recoverWith(ArithmeticException.class,Try.of(() ->  2/0));
        assertEquals("Does not recover of 2/0", Try.of(() -> 2), aTry);
        assertEquals("RecoverWith does not work",
                Try.failure(new ArithmeticException("/ by zero")).toString() ,
                aTry2.toString());
    }
    /**
     *  El Recover retorna el valor a recuperar, pero sin Try, permitiendo que lance un Exception
     *  si, falla
     */
    @Test(expected = ArithmeticException.class)
    public void testTryAndRecover() {
        Try<Integer> aTry = Try.of(() -> 2 / 0).recover(ArithmeticException.class, 2/0);
    }
    /**
     *  Uso de MapTry
     */
    @Test
    public void testTryWithMapTry() {
        CheckedFunction1<Integer,Integer> checkedFunction1 = (CheckedFunction1<Integer, Integer>) s -> {
            Integer result = 0;
            try {
                result = s/0;
            } catch (ArithmeticException e) {
                result = 1;
                //throw e;
            }
            return result;
        };
        Try<Integer> aTry = Try.of(() -> 2).mapTry(checkedFunction1);
        assertEquals("Failed the checkedFuntion", Success(1),aTry);
    }

}
