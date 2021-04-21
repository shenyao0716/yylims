package lims.service;

import ime.calendar.TrigerHandler;
import lims.sync.GetNewEatableClassinfo;

public class NewEatableClassCalander implements TrigerHandler {
    @Override
    public void handle() {
        GetNewEatableClassinfo getNewEatableClassinfo=new GetNewEatableClassinfo();
        getNewEatableClassinfo.handle();
    }
}
