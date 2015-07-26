package xyz.hotchpotch.jutaime.throwable.matchers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import xyz.hotchpotch.jutaime.throwable.Testee;

public class RaiseTest {
    
    private static class TestMatcher extends TypeSafeMatcher<Throwable> {
        @Override
        protected boolean matchesSafely(Throwable t) {
            return false;
        }
        
        @Override
        public void describeTo(Description description) {
            description.appendText("I'm TestMatcher.");
        }
    }
    
    @Test
    public void testRaise1() {
        // インスタンス化の検査
        assertThat(Raise.raise(Throwable.class), instanceOf(Raise.class));
        assertThat(Raise.raise(Exception.class, "message"), instanceOf(Raise.class));
        assertThat(Raise.raise(RuntimeException.class, null), instanceOf(Raise.class));
        assertThat(Raise.raise(new TestMatcher()), instanceOf(Raise.class));
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRaise2() {
        Raise.raise((Class<? extends Throwable>) null);
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRaise3() {
        Raise.raise(null, "message");
    }
    
    @Test(expected  = NullPointerException.class)
    public void testRaise4() {
        Raise.raise((Matcher<Throwable>) null);
    }
    
    @Test
    public void testMatchesSafely() {
        // サブクラスも合格と判定する。
        assertThat(Testee.of(() -> { throw new Exception(); }), not(Raise.raise(RuntimeException.class)));
        assertThat(Testee.of(() -> { throw new RuntimeException(); }), Raise.raise(RuntimeException.class));
        assertThat(Testee.of(() -> { throw new NullPointerException(); }), Raise.raise(RuntimeException.class));
    }
}