package lims.service;

import ime.calendar.TrigerHandler;
import lims.sync.SyncReport;
import lims.sync.SyncSubmitReport;

public class ReportCalander implements TrigerHandler {
    @Override
    public void handle() {
        SyncReport syncReport=new SyncReport();
        syncReport.handle();
        SyncSubmitReport syncSubmitReport=new SyncSubmitReport();
        syncSubmitReport.handle();
    }
}
