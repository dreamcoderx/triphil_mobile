package com.tlicorporation.triphil;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.tlicorporation.triphil.activities.LoginActivity;

class sampleTest {
    @Test
    public void test1() {
        //LoginActivity a = new LoginActivity();

        //assertTrue(a.verifyUser());
        assertTrue(true);
    }
    @Test
    public void test2() {
        fail();
    }

    @Test
    public boolean isMaxReached(Integer ctr, Integer max){
        if (ctr >= max){
            return true;
        }else{
            return false;
        }
    }
}
