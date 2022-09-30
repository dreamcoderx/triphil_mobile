package com.tlicorporation.triphil;

import com.tlicorporation.triphil.activities.ScanActivity;

import org.junit.Test;

public class test1 {

    @Test
    public void TestTrue()
    {

        sampleTest t = new sampleTest();
        boolean output = t.isMaxReached(0,1);
        assert(output);
    }



    @Test
    public void TestFalse(){
        assert(true);
    }
}
