package com.hackthon.demo;

import com.hackthon.demo.clzs.Loc;
import com.hackthon.demo.helper.DistanceHelper;
import com.hackthon.demo.helper.RemoteHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelperTest {
    @Test
    public void testDistanceHelper(){
        Loc loc1 = new Loc(32.9697, -96.80322);
        Loc loc2 = new Loc(29.46786, -98.53506);
        String timeNow = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("RequestRes: " + RemoteHelper.isNormal(timeNow, loc1));
    }
}
