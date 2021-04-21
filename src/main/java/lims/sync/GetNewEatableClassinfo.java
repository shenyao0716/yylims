package lims.sync;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import drp.commons.util.StringUtil;
import lims.service.ReportService;
import lims.util.ConnectUtil;
import lims.util.HttpClientUtil;
import lims.util.ImeIntegrationUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetNewEatableClassinfo {
    private static Logger logger = Logger.getLogger(GetNewEatableClassinfo.class);
    private static String url="http://fsi.nbaic.gov.cn:9001/sampleClass/getSampleClassTree";
    private static String charset = "UTF-8";
    private static int outtimes = 300000;//30
    private static String spjcgxLoginId="zhuby";
    private static String spjcgxPassword="841917";
    public void handle() {
        try{
            logger.info("");
            Session hsession= ImeIntegrationUtils.getSession();
            ConnectUtil connectUtil=new ConnectUtil();
            ReportService reportService=new ReportService();
            Map<String,Object> result=reportService.gettokeninfo(hsession);
            String ramdkey=null;
            String token=null;
            if(result==null){
                String loginresult=connectUtil.getloginConnect(spjcgxLoginId,spjcgxPassword);
                JSONObject rs=JSONObject.parseObject(loginresult);
                if(rs.get("code").equals("400")){
                    logger.error("密码错误");
                }
                else{
                    ramdkey= StringUtil.safeToString(rs.get("randomKey"));
                    token= StringUtil.safeToString(rs.get("token"));
                    int status=reportService.addloginMessage(hsession,token,ramdkey);

                }
            }
            else{
                String time=StringUtil.safeToString(result.get("endtime"));
                Date date=new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String newdate=df.format(date);
                if(newdate.compareTo(time)>0){
                    String loginresult=connectUtil.getloginConnect(spjcgxLoginId,spjcgxPassword);
                    JSONObject rs=JSONObject.parseObject(loginresult);
                    if(rs.get("code").equals("400")){
                        logger.error("密码错误");
                    }
                    else{
                        Long id=Long.parseLong(StringUtil.safeToString(result.get("id")));
                        ramdkey= StringUtil.safeToString(rs.get("randomKey"));
                        token= StringUtil.safeToString(rs.get("token"));
                        int status=reportService.updateloginMessage(hsession,token,ramdkey,id);
                    }
                }
                else {
                    ramdkey= StringUtil.safeToString(result.get("code"));
                    token= StringUtil.safeToString(result.get("token"));
                }
            }
            logger.info("ramdkey---"+ramdkey+"token---"+token);
            this.process(ramdkey,token);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }

    }
    private void process(String ramdkey,String token) throws Exception{
        logger.info("-------增加更新产品类别开始执行--------");
        Session hsession= ImeIntegrationUtils.getSession();
        ReportService reportService=new ReportService();
        hsession.getTransaction().begin();
        Transaction tx = null;
        try {
            Map<String,String> map=new HashMap<String, String>();
            map.put("token",token);
            ConnectUtil connectUtil=new ConnectUtil();
            int flag=reportService.deleteEatableClassinfo(hsession);
            String result= HttpClientUtil.sendGet(url,map,charset,outtimes);

            if(!"".equals(result)&&result!=null) {
                JSONArray jsonArray = JSONArray.parseArray(result);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject firstlevel = jsonArray.getJSONObject(i);
                    String level1 = firstlevel.getString("label");
                    String value = firstlevel.getString("value");
                    Map<String, Object> data = new HashMap<String, Object>();
                        data.put("level1Name", level1);
                        data.put("fullName", level1);
                        data.put("ml_parent", 0);
                        data.put("newFactor_id", value);
                        int status = reportService.AddEatabClasslevel1Info(hsession, data);
                        Map<String,Object> parentIdmap=reportService.getEatabClassByInfo(hsession,value);
                         String level1Id=StringUtil.safeToString(parentIdmap.get("id"));
                         Map<String,Object> levelmap=new HashMap<>();
                         levelmap.put("level1Id",level1Id);
                         levelmap.put("newFactor_id",parentIdmap.get("newFactor_id"));
                         int updatelevel1=reportService.updateEatableClass(hsession,levelmap);
                        if (jsonArray.getJSONObject(i).getJSONArray("children").size() > 0) {
                            JSONArray jsonlevel2 = jsonArray.getJSONObject(i).getJSONArray("children");
                            for (int j = 0; j < jsonlevel2.size(); j++) {
                                JSONObject secondlevel = jsonlevel2.getJSONObject(j);
                                String level2 = secondlevel.getString("label");
                                String value2 = secondlevel.getString("value");
                                    Map<String, Object> ml_parentdata = reportService.getEatabClassByInfo(hsession, StringUtil.safeToString(parentIdmap.get("newFactor_id")));
                                    String fullname = level1 + "/" + level2;
                                    data.put("level2Name", level2);
                                    data.put("fullName", fullname);
                                    data.put("ml_parent", ml_parentdata.get("id").toString());
                                    data.put("newFactor_id", value2);
                                    int status2 = reportService.AddEatabClasslevel2Info(hsession, data);
                                    Map<String,Object> parentIdmap2=reportService.getEatabClassByInfo(hsession,value2);
                                    levelmap.put("level2Id",parentIdmap2.get("id"));
                                    levelmap.put("newFactor_id",parentIdmap2.get("newFactor_id"));
                                    int updatelevel2=reportService.updateEatableClass(hsession,levelmap);
                                    if (jsonlevel2.getJSONObject(j).getJSONArray("children").size() > 0) {
                                        JSONArray jsonlevel3 = jsonlevel2.getJSONObject(j).getJSONArray("children");
                                        for (int k = 0; k < jsonlevel3.size(); k++) {
                                            JSONObject thirdlevel = jsonlevel3.getJSONObject(k);
                                            String level3 = thirdlevel.getString("label");
                                            String value3 = thirdlevel.getString("value");
                                                Map<String, Object> ml_parentdata2 = reportService.getEatabClassByInfo(hsession, StringUtil.safeToString(parentIdmap2.get("newFactor_id")));
                                                String fullname2 = level1 + "/" + level2 + "/" + level3;
                                                data.put("level3Name", level3);
                                                data.put("fullName", fullname2);
                                                data.put("ml_parent", ml_parentdata2.get("id").toString());
                                                data.put("newFactor_id", value3);
                                                int status3 = reportService.AddEatabClasslevel3Info(hsession, data);
                                                Map<String,Object> parentIdmap3=reportService.getEatabClassByInfo(hsession,value3);
                                                levelmap.put("level3Id",parentIdmap3.get("id"));
                                                levelmap.put("newFactor_id",parentIdmap3.get("newFactor_id"));
                                                 int updatelevel3=reportService.updateEatableClass(hsession,levelmap);
                                                if (jsonlevel3.getJSONObject(k).getJSONArray("children").size() > 0) {
                                                    JSONArray jsonlevel4 = jsonlevel3.getJSONObject(k).getJSONArray("children");
                                                    for (int l = 0; l < jsonlevel4.size(); l++) {
                                                        JSONObject forthlevel = jsonlevel4.getJSONObject(l);
                                                        String level4 = forthlevel.getString("label");
                                                        String value4 = forthlevel.getString("value");
                                                            Map<String, Object> ml_parentdata3 = reportService.getEatabClassByInfo(hsession, StringUtil.safeToString(parentIdmap3.get("newFactor_id")));
                                                            String fullname3 = level1+"/"+level2+"/"+level3 + "/"+level4;
                                                            data.put("level4Name", level4);
                                                            data.put("fullName", fullname3);
                                                            data.put("ml_parent", ml_parentdata3.get("id").toString());
                                                            data.put("newFactor_id", value4);
                                                            int status4 = reportService.AddEatabClasslevel4Info(hsession, data);
                                                            Map<String,Object> parentIdmap4=reportService.getEatabClassByInfo(hsession,value4);
                                                            levelmap.put("level4Id",parentIdmap4.get("id"));
                                                            levelmap.put("newFactor_id",parentIdmap4.get("newFactor_id"));
                                                            int updatelevel4=reportService.updateEatableClass(hsession,levelmap);
                                                            if(jsonlevel4.getJSONObject(l).getJSONArray("children").size() > 0){
                                                                JSONArray jsonlevel5 = jsonlevel4.getJSONObject(l).getJSONArray("children");
                                                                for(int n = 0; n < jsonlevel5.size(); n++) {
                                                                    JSONObject fifthlevel5 = jsonlevel5.getJSONObject(n);
                                                                    String level5 = fifthlevel5.getString("label");
                                                                    String value5 = fifthlevel5.getString("value");
                                                                        Map<String, Object> ml_parentdata4 = reportService.getEatabClassByInfo(hsession, StringUtil.safeToString(parentIdmap4.get("newFactor_id")));
                                                                        String fullname4 = level1+"/"+level2+"/"+level3 + "/"+level4+"/"+ level5;
                                                                        data.put("level5Name", level5);
                                                                        data.put("fullName", fullname4);
                                                                        data.put("ml_parent", ml_parentdata4.get("id").toString());
                                                                        data.put("newFactor_id", value5);
                                                                        int status5 = reportService.AddEatabClasslevel5Info(hsession, data);
                                                                         Map<String,Object> parentIdmap5=reportService.getEatabClassByInfo(hsession,value5);
                                                                        levelmap.put("level5Id",parentIdmap5.get("id"));
                                                                        levelmap.put("newFactor_id",parentIdmap5.get("newFactor_id"));
                                                                         int updatelevel5=reportService.updateEatableClass(hsession,levelmap);
                                                            }
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }

                            }
                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ImeIntegrationUtils.closeSession();
        }

        logger.info("-------更新或添加产品分类结束--------");
    }
    public static void main(String[] args)  {
        //SyncSubmitReport syncSubmitRepot=new SyncSubmitReport();
        //syncSubmitRepot.handle();
        GetNewEatableClassinfo getNewEatableClassinfo=new GetNewEatableClassinfo();
        getNewEatableClassinfo.handle();
    }
}
