package mkolaczek.elm;

import org.junit.Test;

import static org.junit.Assert.assertFalse;


public class ElmNamesValidatorTest {
    @Test
    public void isIdentifier() throws Exception {
        assertFalse(new ElmNamesValidator().isIdentifier("123", null));
    }

}