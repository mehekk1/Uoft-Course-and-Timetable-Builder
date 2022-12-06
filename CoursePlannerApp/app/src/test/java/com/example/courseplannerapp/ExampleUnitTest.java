package com.example.courseplannerapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    Model model;

    @Mock
    LoginTabFragment view;

    Presenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new Presenter(view);
    }

    @Test
    public void shouldShowUsernameErrorMessageIfEmpty() throws Exception {

    }

    @Test
    public void testPresenter() {
        //View when then's
        when(view.getEmail()).thenReturn("student@gmail.com");
        when(view.getPass()).thenReturn("password");

        verify(view).loginSuccess(true);
    }

//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }
}