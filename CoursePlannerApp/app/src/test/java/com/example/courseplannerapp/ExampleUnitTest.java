package com.example.courseplannerapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    LoginTabFragment view;

    @Mock
    Model model;

    @Test
    public void testPresenter() {
        //View when then's
        when(view.getEmail()).thenReturn("student@gmail.com");
        when(view.getPass()).thenReturn("password");

        Presenter presenter = new Presenter(view);

        //Model when then's
        doCallRealMethod().when(model).Login("student@gmail.com", "password");

        
    }

//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }
}