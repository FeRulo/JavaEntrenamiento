package co.com.s4n.training.java.vavr;

import io.vavr.collection.List;
import io.vavr.collection.Tree;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TreeSuite {

    @Test(expected = NoSuchElementException.class)
    public void Test0(){
        Tree<String> t = Tree.empty();
        t.head();
    }

    @Test
    public void Test1(){
        Tree<String> t = Tree.of("Fernando");
        assertEquals("Fernando",t.head());
    }

    @Test
    public void Test2(){
        Tree<String> t = Tree.of("Fernando",
                Tree.of("José"), Tree.of("Páez"));
        assertEquals(List.of(Tree.of("José"),Tree.of("Páez")),t.getChildren());
    }

    @Test
    public void Test3(){
        Tree<String> t = Tree.of("Fernando",
                Tree.of("José"), Tree.of("Páez"));
        String s = t.getChildren()
                .head()
                .head();
        assertEquals("José",s);
    }

    @Test
    public void Test4(){
        Tree<String> t = Tree.of("Fernando",
                Tree.of("José"), Tree.of("Páez"));
        assertTrue(t.getChildren().head().isLeaf());

    }

    @Test
    public void Test5(){
        Tree<String> t = Tree.of("Fernando",
                Tree.of("José"), Tree.of("Páez"));
        assertFalse(t.isLeaf());
    }

    @Test
    public void Test6(){
        Tree<String> t = Tree.of("Fernando",
                Tree.of("José", Tree.of("Carvajal")),
                Tree.of("Páez"));
        assertTrue(t.isBranch());
        assertTrue(t.getChildren().head().isBranch());
    }

    @Test
    public void Test7(){
        Tree<String> t = Tree.empty();
        assertFalse(t.isBranch());
        assertFalse(t.isLazy());
    }





}
