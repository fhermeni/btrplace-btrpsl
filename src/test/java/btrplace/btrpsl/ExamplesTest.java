package btrplace.btrpsl;

import btrplace.btrpsl.element.BtrpElement;
import btrplace.model.VM;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Unit tests that check the examples are working.
 *
 * @author Fabien Hermenier
 */
public class ExamplesTest {

    @Test
    public void testHelloVWorld() throws Exception {
        ScriptBuilder sBuilder = new ScriptBuilder();
        Script scr = sBuilder.build(new File("src/main/examples/helloVWorld.btrp"));

        //Basic checker
        Assert.assertEquals(scr.getVMs().size(), 4);
        for (BtrpElement e : scr.getVMs()) {
            Assert.assertEquals(e.getElement().getClass(), VM.class);
            VM vm = (VM) e.getElement();
            Assert.assertEquals(scr.getAttributes().get(vm, "template"), "tiny");
        }
        Assert.assertEquals(scr.getConstraints().size(), 2);
    }

    @Test
    public void testMultipleAssignment() throws Exception {
        ScriptBuilder sBuilder = new ScriptBuilder();
        Script scr = sBuilder.build(new File("src/main/examples/multipleAssignment.btrp"));
        //Basic checker
        Assert.assertEquals(scr.getVMs().size(), 6);
        for (BtrpElement e : scr.getVMs()) {
            Assert.assertEquals(e.getElement().getClass(), VM.class);
            VM vm = (VM) e.getElement();
            Assert.assertEquals(scr.getAttributes().get(vm, "template"), "tinyInstance");
        }
        Assert.assertEquals(scr.getConstraints().size(), 0);
        Assert.assertEquals(scr.getExported().size(), 7);

    }
}

