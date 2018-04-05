package co.com.s4n.training.java;
import co.com.s4n.training.java.Concatenador;

public class ServiceClient {

    public static String useMethod(Concatenador concatenador, String s1, String s2){
        return concatenador.concat(s1,s2);
    }
    public static String useMethod2(Concatenador concatenador, String s1, String s2){
        return concatenador.concat2(s1,s2);
    }
    public static boolean imprimir(){
        return true;
    }
}
