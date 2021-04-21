package lims.service;

import drp.commons.util.StringUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportService {

    public List<Map<String, Object>> getPropery(Session session, String name) {
        List<Map<String, Object>> result = null;
        List<Map<String, Object>> list = null;
        String sql = "select  * from DYNA_SJ_Property where name=:name";
        Query query = session.createSQLQuery(sql);
        query.setString("name", name);
        list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = (List<Map<String, Object>>) list;
        }
        return result;
    }

    public List<Map<String, Object>> getReportByplatformId(Session session, long platformId) {
        List<Map<String, Object>> result = null;
        List<Map<String, Object>> list = null;
        String sql = "select id, syncState, WF_State from DYNA_SJ_Report where platform_id=:platform_id";
        Query query = session.createSQLQuery(sql);
        query.setLong("platform_id", platformId);
        list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = (List<Map<String, Object>>) list;
        }
        return result;
    }

    public int updateReportStatusById(Session session, String message, long syncState, long id) {
        String sql = "update DYNA_SJ_Report set syncState=:syncState, submitType=:submitType, message=:message where id=:id";
        Query query = session.createSQLQuery(sql);
        query.setLong("syncState", syncState);
        query.setParameter("message", message);
        query.setParameter("submitType", 1L);
        query.setParameter("id", id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }

    public int updatePropertyById(Session session, long id, String endtime) {
        String sql = "update DYNA_SJ_Property  set val=? where id=? ";
        Query query = session.createSQLQuery(sql);
        query.setString(0, endtime);
        query.setLong(1, id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return  status;
    }

    public int updatePropertyById(Session session, long id) {
        String sql = "update DYNA_SJ_Property  set val=null where id=? ";
        Query query = session.createSQLQuery(sql);
        query.setLong(0, id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return  status;
    }

    //查询本地实验室系统中的SJ_Report表 已批准 未同步 的报告数据
    public List<Map<String,Object>> getNoSyncReportList(Session session) {
        List<Map<String, Object>> result = null;
        List<Map<String, Object>> list = null;
        String sql = "select * from DYNA_SJ_Report where WF_State=5 and syncState=1 and taskSource=1 and  plantype is NULL   ";
        Query query = session.createSQLQuery(sql);
        list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = (List<Map<String, Object>>) list;
        }
        System.out.println();
        return result;
    }
    //通过id获取产品的类型
    public Map<String,Object>getEatableClassById(Session session,long id){
        List<Map<String, Object>> list = null;
        Map<String,Object> result=new HashMap<>();
        String sql="select name,platform_id,codeNo from DYNA_SJ_EatableClass where id=?";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,id);
        list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return  result;
    }
    //获取检测项目
    public List<Map<String,Object>> getCheckItemlist(Session session, long reportId){
        List<Map<String, Object>> result = null;
        List<Map<String, Object>> list = null;
        String sql = "select * from DYNA_SJ_CheckItem where report_id=? and (isChecking is null or isChecking='true' )  ";
        Query query = session.createSQLQuery(sql);
        query.setLong(0,reportId);
        list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = (List<Map<String, Object>>) list;
        }
        return result;
    }
    //同步报告成功
    public int updateReportSyncStateSuccess(Session session, long reportId){
        String sql = "update DYNA_SJ_Report set  syncState=2, message=null where id=?   ";
        Query query = session.createSQLQuery(sql);
        query.setLong(0,reportId);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return  status;
    }
    //同步报告失败
    public int updateReportSyncStatefailed(Session session, String message,long reportId){
        String sql = "update DYNA_SJ_Report set  syncState=3, message=? where id=?   ";
        Query query = session.createSQLQuery(sql);
        query.setString(0,message);
        query.setLong(1,reportId);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return  status;
    }

    //
    public Map<String,Object> getAreaInfoById(Session session,long id){
        Map<String, Object> result = null;
        List<Map<String, Object>> list = null;
        String sql = "select * from DYNA_SJ_Area where id =?";
        Query query = session.createSQLQuery(sql);
        query.setLong(0,id);
        list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return  result;
    }
    public Map<String,Object> getAreainfo(Session session ,long areaId,long id){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_RelatedCompany where area_id=? and id=?";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,areaId);
        query.setLong(1,id);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public Map<String,Object> gettokeninfo(Session session){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_loginMessage";
        Query query=session.createSQLQuery(sql);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public int  addloginMessage(Session session,String token,String code){
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)+5);
        Date date=curr.getTime();
        Map<String,Object> result=null;
        String sql="insert into DYNA_SJ_loginMessage (token,endtime,code) values(?,?,?)";
        Query query = session.createSQLQuery(sql);
        query.setString(0,token);
        query.setTimestamp(1,date);
        query.setString(2,code);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public int  updateloginMessage(Session session,String token,String code,long id){
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH,curr.get(Calendar.DAY_OF_MONTH)+7);
        Date date=curr.getTime();
        Map<String,Object> result=null;
        String sql="update DYNA_SJ_loginMessage set token=?,code=?,endtime=? where id=? ";
        Query query = session.createSQLQuery(sql);
        query.setString(0,token);
        query.setString(1,code);
        query.setTimestamp(2,date);
        query.setLong(3,id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public  List<Map<String,Object>> getReportData(Session session){
        List<Map<String,Object>> list=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)-2);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1= df.format(calendar.getTime());
        String date2=df.format(new Date());
        String sql="select * from DYNA_ReportFile where  FM_UpdateTime>=? and FM_UpdateTime<=?";
        Query query=session.createSQLQuery(sql);
        query.setString(0,date1);
        query.setString(1,date2);
        list =  query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return  list;
    }
    public Map<String,Object> getoldLevelName(Session session,String name,String specialType){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_NewEatableClass where finalName=? and specialType=?";
        Query query=session.createSQLQuery(sql);
        query.setString(0,name);
        query.setString(1,specialType);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public Map<String,Object> getEatabClassByInfo(Session session,String value){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_EatableNewClass where newFactor_id=?";
        Query query=session.createSQLQuery(sql);
        query.setString(0,value);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public int AddEatabClasslevel1Info(Session session,Map<String,Object> map){
        Map<String,Object> result=null;
        String level1Name= StringUtil.safeToString(map.get("level1Name"));
        long ml_parent=Long.parseLong(StringUtil.safeToString(map.get("ml_parent")));
        String fullName=StringUtil.safeToString(map.get("fullName"));
        String newFactor_id=StringUtil.safeToString(map.get("newFactor_id"));
        long datetime=Calendar.getInstance().getTimeInMillis();
        String sql="insert into DYNA_SJ_EatableNewClass (domainId,ml_parent,_ml_level_,_ml_code_,name,fullName,state,level1Name,newFactor_id) values(?,?,?,?,?,?,?,?,?)";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,1);
        query.setLong(1,ml_parent);
        query.setInteger(2,1);
        query.setLong(3,datetime);
        query.setString(4,level1Name);
        query.setString(5,fullName);
        query.setString(6,"1");
        query.setString(7,level1Name);
        query.setString(8,newFactor_id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public int AddEatabClasslevel2Info(Session session,Map<String,Object> map){
        Map<String,Object> result=null;
        String level1Name= StringUtil.safeToString(map.get("level1Name"));
        String level2Name= StringUtil.safeToString(map.get("level2Name"));
        long ml_parent=Long.parseLong(StringUtil.safeToString(map.get("ml_parent")));
        String fullName=StringUtil.safeToString(map.get("fullName"));
        String newFactor_id=StringUtil.safeToString(map.get("newFactor_id"));
        long datetime=Calendar.getInstance().getTimeInMillis();
        String sql="insert into DYNA_SJ_EatableNewClass (domainId,ml_parent,_ml_level_,_ml_code_,name,fullName,state,level1Name,level2Name,newFactor_id) values(?,?,?,?,?,?,?,?,?,?)";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,1);
        query.setLong(1,ml_parent);
        query.setInteger(2,2);
        query.setLong(3,datetime);
        query.setString(4,level2Name);
        query.setString(5,fullName);
        query.setString(6,"1");
        query.setString(7,level1Name);
        query.setString(8,level2Name);
        query.setString(9,newFactor_id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public int AddEatabClasslevel3Info(Session session,Map<String,Object> map){
        Map<String,Object> result=null;
        String level1Name= StringUtil.safeToString(map.get("level1Name"));
        String level2Name= StringUtil.safeToString(map.get("level2Name"));
        String level3Name= StringUtil.safeToString(map.get("level3Name"));
        long ml_parent=Long.parseLong(StringUtil.safeToString(map.get("ml_parent")));
        long datetime=Calendar.getInstance().getTimeInMillis();
        String fullName=StringUtil.safeToString(map.get("fullName"));
        String newFactor_id=StringUtil.safeToString(map.get("newFactor_id"));
        String sql="insert into DYNA_SJ_EatableNewClass (domainId,ml_parent,_ml_level_,_ml_code_,name,fullName,state,level1Name,level2Name,level3Name,newFactor_id) values(?,?,?,?,?,?,?,?,?,?,?)";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,1);
        query.setLong(1,ml_parent);
        query.setInteger(2,3);
        query.setLong(3,datetime);
        query.setString(4,level3Name);
        query.setString(5,fullName);
        query.setString(6,"1");
        query.setString(7,level1Name);
        query.setString(8,level2Name);
        query.setString(9,level3Name);
        query.setString(10,newFactor_id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public int AddEatabClasslevel4Info(Session session,Map<String,Object> map){
        Map<String,Object> result=null;
        String level1Name= StringUtil.safeToString(map.get("level1Name"));
        String level2Name= StringUtil.safeToString(map.get("level2Name"));
        String level3Name= StringUtil.safeToString(map.get("level3Name"));
        String level4Name= StringUtil.safeToString(map.get("level4Name"));
        long ml_parent=Long.parseLong(StringUtil.safeToString(map.get("ml_parent")));
        long datetime=Calendar.getInstance().getTimeInMillis();
        String fullName=StringUtil.safeToString(map.get("fullName"));
        String newFactor_id=StringUtil.safeToString(map.get("newFactor_id"));
        String sql="insert into DYNA_SJ_EatableNewClass (domainId,ml_parent,_ml_level_,_ml_code_,name,fullName,state,level1Name,level2Name,level3Name,level4Name,newFactor_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,1);
        query.setLong(1,ml_parent);
        query.setInteger(2,4);
        query.setLong(3,datetime);
        query.setString(4,level4Name);
        query.setString(5,fullName);
        query.setString(6,"1");
        query.setString(7,level1Name);
        query.setString(8,level2Name);
        query.setString(9,level3Name);
        query.setString(10,level4Name);
        query.setString(11,newFactor_id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public int AddEatabClasslevel5Info(Session session,Map<String,Object> map){
        Map<String,Object> result=null;
        String level1Name= StringUtil.safeToString(map.get("level1Name"));
        String level2Name= StringUtil.safeToString(map.get("level2Name"));
        String level3Name= StringUtil.safeToString(map.get("level3Name"));
        String level4Name= StringUtil.safeToString(map.get("level4Name"));
        String level5Name= StringUtil.safeToString(map.get("level5Name"));
        long ml_parent=Long.parseLong(StringUtil.safeToString(map.get("ml_parent")));
        long datetime=Calendar.getInstance().getTimeInMillis();
        String fullName=StringUtil.safeToString(map.get("fullName"));
        String newFactor_id=StringUtil.safeToString(map.get("newFactor_id"));
        String sql="insert into DYNA_SJ_EatableNewClass (domainId,ml_parent,_ml_level_,_ml_code_,name,fullName,state,level1Name,level2Name,level3Name,level4Name,level5Name,newFactor_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,1);
        query.setLong(1,ml_parent);
        query.setInteger(2,5);
        query.setLong(3,datetime);
        query.setString(4,level5Name);
        query.setString(5,fullName);
        query.setString(6,"1");
        query.setString(7,level1Name);
        query.setString(8,level2Name);
        query.setString(9,level3Name);
        query.setString(10,level4Name);
        query.setString(11,level5Name);
        query.setString(12,newFactor_id);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public Map<String,Object> getcompanyinfo(Session session ,long id){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_RelatedCompany where  id=?";
        Query query=session.createSQLQuery(sql);
        query.setLong(0,id);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public int deleteEatableClassinfo(Session session){
        Map<String,Object> result=null;
        String sql="delete from DYNA_SJ_EatableNewClass where state=1";
        Query query=session.createSQLQuery(sql);
        session.beginTransaction();
        int status = query.executeUpdate();
        session.getTransaction().commit();
        return status;
    }
    public int updateEatableClass(Session session,Map<String,Object> map){
        Map<String,Object> result=null;
        String levelid1=StringUtil.safeToString(map.get("level1Id"));
        String levelid2=StringUtil.safeToString(map.get("level2Id"));
        String levelid3=StringUtil.safeToString(map.get("level3Id"));
        String levelid4=StringUtil.safeToString(map.get("level4Id"));
        String levelid5=StringUtil.safeToString(map.get("level5Id"));
        String newFactor_id=StringUtil.safeToString(map.get("newFactor_id"));
        int status=0;
        if("".equals(levelid2)){
            String sql="update DYNA_SJ_EatableNewClass set level1Id=? where newFactor_id=?";
            Query query=session.createSQLQuery(sql);
            query.setLong(0,Long.parseLong(levelid1));
            query.setString(1,newFactor_id);
            session.beginTransaction();
             status = query.executeUpdate();
            session.getTransaction().commit();
        }else {
            if("".equals(levelid3)){
                String sql="update DYNA_SJ_EatableNewClass set level1Id=?,level2Id=? where newFactor_id=?";
                Query query=session.createSQLQuery(sql);
                query.setLong(0,Long.parseLong(levelid1));
                query.setLong(1,Long.parseLong(levelid2));
                query.setString(2,newFactor_id);
                session.beginTransaction();
                status = query.executeUpdate();
                session.getTransaction().commit();
            }else {
                if("".equals(levelid4)){
                    String sql="update DYNA_SJ_EatableNewClass set level1Id=?,level2Id=?,level3Id=? where newFactor_id=?";
                    Query query=session.createSQLQuery(sql);
                    query.setLong(0,Long.parseLong(levelid1));
                    query.setLong(1,Long.parseLong(levelid2));
                    query.setLong(2,Long.parseLong(levelid3));
                    query.setString(3,newFactor_id);
                    session.beginTransaction();
                    status = query.executeUpdate();
                    session.getTransaction().commit();
                }else{
                    if("".equals(levelid5)){
                        String sql="update DYNA_SJ_EatableNewClass set level1Id=?,level2Id=?,level3Id=?,level4Id=? where newFactor_id=?";
                        Query query=session.createSQLQuery(sql);
                        query.setLong(0,Long.parseLong(levelid1));
                        query.setLong(1,Long.parseLong(levelid2));
                        query.setLong(2,Long.parseLong(levelid3));
                        query.setLong(3,Long.parseLong(levelid4));
                        query.setString(4,newFactor_id);
                        session.beginTransaction();
                        status = query.executeUpdate();
                        session.getTransaction().commit();
                    }
                    else {
                            String sql="update DYNA_SJ_EatableNewClass set level1Id=?,level2Id=?,level3Id=?,level4Id=?,level5Id=? where newFactor_id=?";
                            Query query=session.createSQLQuery(sql);
                            query.setLong(0,Long.parseLong(levelid1));
                            query.setLong(1,Long.parseLong(levelid2));
                            query.setLong(2,Long.parseLong(levelid3));
                            query.setLong(3,Long.parseLong(levelid4));
                            query.setLong(4,Long.parseLong(levelid5));
                            query.setString(5,newFactor_id);
                            session.beginTransaction();
                            status = query.executeUpdate();
                            session.getTransaction().commit();
                        }
                }
            }
        }


        return status;
    }
    public Map<String,Object> gettest(Session session){
        Map<String,Object> result=new HashMap<>();
        String sql="select sendTime from dyna_sj_report where id=11";
        Query query=session.createSQLQuery(sql);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public Map<String,Object> getItemClass(Session session,String checkItem){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_ItemEatableClass where itemName=?";
        Query query=session.createSQLQuery(sql);
        query.setString (0,checkItem);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }
    public Map<String,Object> getCityItemClass(Session session,String checkItem){
        Map<String,Object> result=null;
        String sql="select * from DYNA_SJ_CityCheckItem where name=?";
        Query query=session.createSQLQuery(sql);
        query.setString (0,checkItem);
        List<Map<String, Object>>  list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

}
