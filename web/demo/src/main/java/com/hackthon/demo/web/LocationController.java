package com.hackthon.demo.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackthon.demo.clzs.*;
import com.hackthon.demo.helper.DistanceHelper;
import com.hackthon.demo.helper.RemoteHelper;
import com.hackthon.demo.helper.TimeHelper;
import com.hackthon.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.stream.Location;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;



@Controller
@RequestMapping(value = "/")
public class LocationController {
    private final String DEFAULTEMAILL = "softwaredeveloper_shy@outlook.com";

    @Autowired
    private EmailService emailService;
    ArrayList<PriorityLoc> path = new ArrayList<>();
    Loc bucket[] = new Loc[1440];
    String email;

    ArrayList<SensitiveLoc> sensitiveLoc;
    double threshold = 1000.0;

    double supportVector[] = new double[5];     // [0] electricity normal, without post? [1] near to hotspot? [2] leave schLoc? [3] abnormal path ? [4] abnormal time?
    double weightedVector[] = {1.0, 1.0, 1.0, 1.0, 0.1};

    double sentPriority = 0;

    public LocationController(){
        sensitiveLoc = new ArrayList<>();
        sensitiveLoc.add(new SensitiveLoc(40.45167180000001,-79.95153379999999, 3));    // cvs (centre ave)
        sensitiveLoc.add(new SensitiveLoc(40.43655890000001,-79.92453109999997, 10));    // squirrel hill
        sensitiveLoc.add(new SensitiveLoc(40.4425525,-79.95411380000002, 8));           // hillman lib
        sensitiveLoc.add(new SensitiveLoc(40.427180000001,-79.91379999999, 13));
        sensitiveLoc.add(new SensitiveLoc(40.47180000001,-79.9179999999, 13));

//        path.add(new PriorityLoc(10.0, 20.0, 0.4, true));
//        sensitiveLoc.add(new SensitiveLoc(40.527601, -80.053485, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.532517, -80.093538, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.557701, -80.039728, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.591972, -80.048050, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.557870, -80.093080, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.578854, -80.052784, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.536789, -80.044004, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.531838, -80.079121, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.534195, -80.032267, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.506405, -80.005637, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.503483, -80.072042, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.580812, -80.028934, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.504653, -80.032014, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.587152, -80.098831, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.507132, -80.059128, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.536578, -80.010938, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.552754, -80.081903, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.598024, -80.066935, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.545307, -80.084814, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.565538, -80.066443, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.597109, -80.090595, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.561399, -80.076041, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.540142, -80.073889, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.552795, -80.037117, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.571686, -80.092838, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.554733, -80.050976, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.555170, -80.081868, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.574236, -80.060189, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.528385, -80.049250, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.536184, -80.021866, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.527216, -80.034934, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.554357, -80.083744, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.515808, -80.017365, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.533183, -80.001719, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.507330, -80.097440, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.500248, -80.017208, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.540220, -80.057765, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.540820, -80.022914, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.559124, -80.096545, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.588018, -80.026619, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.589566, -80.011434, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.500950, -80.092770, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.572558, -80.095390, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.547252, -80.018004, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.569829, -80.057021, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.509590, -80.031106, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.510476, -80.082376, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.598620, -80.024325, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.549207, -80.012756, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.682586, -80.166491, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.665625, -80.119729, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.608860, -80.180697, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.644836, -80.172498, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.638161, -80.130226, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.682579, -80.163236, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.631297, -80.152885, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.695993, -80.196099, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.624321, -80.175191, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.691696, -80.140743, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.657402, -80.116259, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.664185, -80.115270, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.601673, -80.127402, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.687351, -80.126327, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.674911, -80.129359, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.609096, -80.144993, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.641439, -80.131503, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.637912, -80.155634, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.613496, -80.197306, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.650334, -80.127788, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.673736, -80.117706, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.643985, -80.129267, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.634017, -80.192435, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.601003, -80.155810, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.693676, -80.148777, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.647941, -80.137808, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.620326, -80.159082, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.665139, -80.179750, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.633640, -80.175594, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.667474, -80.172704, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.670290, -80.152883, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.679332, -80.185865, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.647954, -80.141811, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.643810, -80.105225, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.664822, -80.176032, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.692457, -80.104023, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.619684, -80.111532, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.657340, -80.157702, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.620027, -80.112416, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.691195, -80.183111, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.642749, -80.168633, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.653392, -80.172192, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.625246, -80.111577, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.617790, -80.112185, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.632286, -80.126436, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.655171, -80.105168, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.687733, -80.120692, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.680749, -80.122375, 5));
//        sensitiveLoc.add(new SensitiveLoc(40.612761, -80.112251, 5));
         /* Test cases */
//        path.add(new PriorityLoc(40.4167180000001,-79.85153379994999, 2, true));
//        path.add(new PriorityLoc(40.45167180000001,-79.95153379999999, 7, true));
//        path.add(new PriorityLoc(40.45167140000001,-79.95153329999999,0, false));
//        path.add(new PriorityLoc(40.35167180000001,-79.75153379999999, 2, true));
//        path.add(new PriorityLoc(40.4516180000001,-79.7513379999999, 2, true));
//        path.add(new PriorityLoc(40.5167180000001,-79.7153379999999, 5,true));
//        path.add(new PriorityLoc(40.5267180000001,-79.75379999999, 9, true));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHome() {
        return "index";
    }
    @RequestMapping(value = "/setSchedule", method = RequestMethod.GET)
    public String getSchedulePage(){
        return "setSchedule";
    }

    @RequestMapping(value = "/setSchedule", method = RequestMethod.POST)
    public ResponseEntity<Object> setSchedule(@RequestBody Info bd) throws IOException {
        List<Sch> schs = bd.getSchs();
        email = bd.getEmail();
        for (Sch s : schs){
            System.out.println(s.getStart() + " : " + s.getStop() + " : "+ s.getLocation());
            for (int i = TimeHelper.timeToBucket(s.getStart()); i < TimeHelper.timeToBucket(s.getStop()); i ++){
                bucket[i] = s.getLocation();
            }
        }
        return new ResponseEntity<Object>(bd, HttpStatus.OK);
    }



    @RequestMapping(value = "/postLocation", method = RequestMethod.POST)
    public String postLocation(@RequestBody Loc loc){
        // do check range
        // get now
        System.out.println(loc.toString());
        String now = new SimpleDateFormat("hh:mm").format(Calendar.getInstance().getTime());
        String timeNow = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss").format(Calendar.getInstance().getTime());
        int timeStamp = TimeHelper.timeToBucket(now);
        // get scheduled location
        Loc scheduledLoc = bucket[timeStamp];
        double priority = 0;
        boolean show = true;
        // [0] electricity normal, without post? [1] near to hotspot? [2] leave schLoc? [3] abnormal path ? [4] abnormal time?
        if (scheduledLoc != null){
            double dis = DistanceHelper.distance(scheduledLoc, loc);
            if (dis > threshold){
                // alert, send email once
                supportVector[2] = 1.0;
                supportVector[1] = inTraffickingRange(loc);
            } else show = false;
//        }else if (!RemoteHelper.isNormal(timeNow, loc)){
//            supportVector[3] = 1.0;
//            supportVector[1] = inTraffickingRange(loc);
        }
        // compute the priority
        for (int i = 0; i < supportVector.length; i ++){
            priority += supportVector[i] * weightedVector[i];
        }
        priority = supportVector[2] != 0 ? 1.0 / (1 + Math.exp(-priority)) : 0;
        if (sentPriority < priority) {
            emailService.sendSimpleEmail(email, new Loc(0,0), priority);
        }
        sentPriority = priority;
        path.add(new PriorityLoc(loc, priority,show));
        return "done";
    }

    private int inTraffickingRange(Loc loc) {
        int res = 0;
        for (Loc loc1 : sensitiveLoc){
            res += DistanceHelper.distance(loc, loc1) > threshold ? 1 : 0;
        }
        return res;
    }


    @RequestMapping(value = "/viewActivity", method = RequestMethod.GET)
    public ModelAndView viewActivity(){

        ModelAndView model = new ModelAndView("viewActivity");
        model.addObject("hello", "Hello");
        model.addObject("path", path);
        model.addObject("sensitiveLoc", sensitiveLoc);
        return model;
    }

    @RequestMapping(value = "/testMap", method = RequestMethod.GET)
    public String testMap(){
        return "testMap";
    }

    @RequestMapping(value = "/selectMap/{id}", method = RequestMethod.GET)
    public ModelAndView selectMap(@PathVariable String id){
        ModelAndView model = new ModelAndView("selectMap");
        model.addObject("id", id);
        return model;
    }

    @RequestMapping(value = "/getPath", method = RequestMethod.GET)
    public ResponseEntity<Object> getPath(){
        return new ResponseEntity<Object>(path, HttpStatus.OK);
    }

    @RequestMapping(value = "/contactUs", method = RequestMethod.GET)
    public String contactUs(){
        return "contactUs";
    }
}
