package lims.util;

import apf.crypto.Base64;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import drp.commons.util.StringUtil;
import lims.service.ReportService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectUtil {
    private static Logger logger = Logger.getLogger(ConnectUtil.class);
    public String getloginConnect(String var1, String var2) {

        String responseStr = "";
        JSONObject login = new JSONObject();
        login.put("userName", var1);
        login.put("password", var2);
        try{
            String loginvalue = login.toJSONString();
            byte[] textByte = loginvalue.getBytes();
            String base64encodedString = Base64.encode(textByte);
            String md5 = MD5Util.string2MD5(base64encodedString);
            JSONObject jb = new JSONObject();
            jb.put("object", base64encodedString);
            jb.put("sign", md5);
            String sb = jb.toJSONString();
            String sb1 = sb.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/auth", sb1);
            //responseStr = HttpClientUtil.sendPost("http://101.132.159.222:13010//auth", sb);

        }catch (Exception e) {
            e.printStackTrace();
        }

        //responseStr=HttpClient.sendpost(sb,"http://fsi-api.govsz.com//auth");
        System.out.println(responseStr);
        return responseStr;
    }

    public JSONArray  getSerachReportConnect(String start,String  end,int state,String randomKey, String token) throws UnsupportedEncodingException {
        String responseStr = "";
        JSONArray array = new JSONArray();
        try{
            JSONObject json = new JSONObject();
            JSONObject jb=new JSONObject();
            jb.put("current",1);
            jb.put("order","false");
            jb.put("size","20");
            jb.put("sort","");
            jb.put("total",0);
            json.put("page",jb);
            JSONObject jb2=new JSONObject();
            jb2.put("start",start);
            jb2.put("end", end);
            json.put("date",jb2);
            json.put("state", state);
            json.put("tabName","报告录入");
            //json.put("token", token);
            logger.info(json+"json");
            String jsonvalue = json.toJSONString();
            byte[] textByte = jsonvalue.getBytes("UTF-8");
            StringBuffer sb = new StringBuffer();
            String base64encodedString = Base64.encode(textByte);
            sb.append(base64encodedString).append(randomKey);
            String sbl = String.valueOf(sb);
            //String sbreplace = sbl.replace("\r\n", "");
            String md5 = MD5Util.string2MD5(String.valueOf(sbl));
            JSONObject jsonb = new JSONObject();
            jsonb.put("object", base64encodedString);
            jsonb.put("sign", md5);
            String connectValue = jsonb.toJSONString();
            String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
            logger.info("查询报告请求参数:"+connectValue);
            System.out.println(token+"token");
            System.out.println(sb1+"查询参数");
            try {
                //responseStr = HttpClientUtil.sendPost("http://fsi-api.govsz.com/inputReport/queryInputReportList",token, connectValue);
                responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/inputReport/queryInputReportList",token, connectValue);
                System.out.println(responseStr);
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                array = jsonObject.getJSONArray("records");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Throwable ex){
            StringWriter stringWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(stringWriter));
            logger.info("查询接口报错=="+stringWriter.toString());
            //logger.info("上报接口报错"+ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
        return array;
    }

    public String getConnFactorId(String var1,String randomKey,String token) throws UnsupportedEncodingException{
        String responseStr = "";
        JSONObject json = new JSONObject();
        //json.put("name", var1);
        //String jsonvalue = json.toJSONString();
        StringBuffer stringBuffer=new StringBuffer();
        //String jsonvalue="甲醇";
        String jsonvalue=var1;
        jsonvalue="\""+(String)jsonvalue  + "\"";
        logger.info("--------getfactorId-data---------"+jsonvalue);
        byte[] textByte = jsonvalue.getBytes("utf-8");
        StringBuffer sb = new StringBuffer();
        String base64encodedString = Base64.encode(textByte);
        sb.append(base64encodedString).append(randomKey);
        String sbl = String.valueOf(sb);
        String sbreplace = sbl.replace("\r\n", "");
        String md5 = MD5Util.string2MD5(String.valueOf(sbl));
        JSONObject jb = new JSONObject();
        jb.put("object", base64encodedString);
        jb.put("sign", md5);
        String connectValue = jb.toJSONString();
        System.out.println(connectValue);
        String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
        try {
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/factorClass/getFactorClassByName",token, connectValue);
            //responseStr = HttpClientUtil.sendPost("http://47.111.28.93:13021/factorClass/getFactorClassByName",token, connectValue);
            logger.info("---return getFactorClassByName data----"+responseStr);
            if(responseStr!=""&&responseStr!=null){
                JSONObject jsonObject=JSONObject.parseObject(responseStr);
                responseStr=jsonObject.get("id").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }
    public String getConnCompanyId(String var1,String randomKey,String token) throws UnsupportedEncodingException{
        String responseStr = "";
        String result="";
        //JSONObject json = new JSONObject();
        //json.put("name", var1);
        //String jsonvalue = json.toJSONString();
        //logger.info("--------getconnCompanyId-data---------"+jsonvalue);
        //String jsonvalue=var1;
        //jsonvalue="\""+(String)jsonvalue  + "\"";
        String jsonvalue=var1;
        jsonvalue="\""+(String)jsonvalue  + "\"";
        logger.info("--------getconnCompanyId-data---------"+jsonvalue);
        byte[] textByte = jsonvalue.getBytes("utf-8");
        StringBuffer sb = new StringBuffer();
        String base64encodedString = Base64.encode(textByte);
        sb.append(base64encodedString).append(randomKey);
        String sbl = String.valueOf(sb);
        String sbreplace = sbl.replace("\r\n", "");
        String  md5 = MD5Util.string2MD5(String.valueOf(sbreplace));
        JSONObject jb = new JSONObject();
        jb.put("object", base64encodedString);
        jb.put("sign", md5);
        String connectValue = jb.toJSONString();
        String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
        try {
            System.out.println(connectValue+connectValue);
            //responseStr = HttpClientUtil.sendPost("http://fsi-api.govsz.com/unit/getUnitByName",token, connectValue);
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/unit/getUnitByName",token, connectValue);
            logger.info("--------return ConnCompanyId message---------"+responseStr);
            System.out.println(responseStr);
            if(responseStr!=""&&responseStr!=null) {
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                result = jsonObject.get("id").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public String getConnproductId(String var1, long mlevle, String randomKey, String token) throws UnsupportedEncodingException{
        String responseStr = "";
        String result="";
        JSONObject json = new JSONObject();
        json.put("name", var1);
        json.put("mlLevel",mlevle);
        String jsonvalue = json.toJSONString();
        logger.info("-------getConnproductId-date-------"+json);
        byte[] textByte = jsonvalue.getBytes("utf-8");
        StringBuffer sb = new StringBuffer();
        String base64encodedString = Base64.encode(textByte);
        sb.append(base64encodedString).append(randomKey);
        String sbl = String.valueOf(sb);
        String sbreplace = sbl.replace("\r\n", "");
        String md5 = MD5Util.string2MD5(String.valueOf(sbl));
        JSONObject jb = new JSONObject();
        jb.put("object", base64encodedString);
        jb.put("sign", md5);
        String connectValue = jb.toJSONString();
        String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
        try {
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/eatClass/getFactorClassByName",token, connectValue);
            //responseStr = HttpClientUtil.sendPost("http://fsi-api.govsz.com/eatClass/getFactorClassByName",token, connectValue);
            logger.info("-----return getFactorClassByName message----"+responseStr);
            System.out.println(responseStr);
            if(responseStr!=""&&responseStr!=null){
                JSONObject jsonObject=JSONObject.parseObject(responseStr);
                responseStr=jsonObject.get("id").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }
    public String getConnPlanId(String var1,String randomKey,String token) throws UnsupportedEncodingException{
        String responseStr = "";
        JSONObject json = new JSONObject();
        //json.put("name", var1);
        //String jsonvalue = json.toJSONString();
        String jsonvalue=var1;
        jsonvalue="\""+(String)jsonvalue  + "\"";
        byte[] textByte = jsonvalue.getBytes("utf-8");
        StringBuffer sb = new StringBuffer();
        String base64encodedString = Base64.encode(textByte);
        String base64 = base64encodedString.replace("\r\n", "");
        sb.append(base64).append(randomKey);
        String sbl = String.valueOf(sb);
        String sbreplace = sbl.replace("\r\n", "");
        String md5 = MD5Util.string2MD5(String.valueOf(sbreplace));
        JSONObject jb = new JSONObject();
        jb.put("object", base64);
        jb.put("sign", md5);
        String connectValue = jb.toJSONString();
        String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
        try {
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/reportPlan/getReportPlanByName",token, connectValue);
            // responseStr = HttpClientUtil.sendPost("http://fsi-api.govsz.com/reportPlan/getReportPlanByName",token, connectValue);
            System.out.println(responseStr);
            JSONObject jsonObject=JSONObject.parseObject(responseStr);
            responseStr=jsonObject.get("planNum").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }
    public String addCompanyId(Map<String,Object> map,String randomKey,String token,int type)throws UnsupportedEncodingException{
        logger.info("----addCompanyId----"+map);
        String responseStr = "";
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject jb=new JSONObject();
        ReportService reportService=new ReportService();
        if(type==1){
            jb.put("isClient","1");
        }
        else if(type==2){
            jb.put("isDetection","1");
        }
        else if(type==3){
            jb.put("isGovernment","1");
        }
        else if(type==4){
            jb.put("isInspected","1");
        }
        else if(type==5){
            jb.put("isProducer","1");
        }
        else {
            jb.put("isSender","1");
        }
        jb.put("legalPerson",MapUtils.getString(map,"legalPerson"));
        jb.put("locked",MapUtils.getString(map,"locked"));
        jb.put("areaId",MapUtils.getString(map,"area_id"));
        jb.put("address",MapUtils.getString(map,"address"));
        jb.put("fax",MapUtils.getString(map,"fax"));
        jb.put("phone",MapUtils.getString(map,"phone"));
        jb.put("name",MapUtils.getString(map,"name"));
        jb.put("phase",MapUtils.getString(map,"phase"));
        jb.put("postcode",MapUtils.getString(map,"postcode"));
        jb.put("remark",MapUtils.getString(map,"remark"));
        //json.put("token", token);
        String jsonvalue = jb.toJSONString();
        logger.info("addcompany---data----"+jsonvalue);
        byte[] textByte = jsonvalue.getBytes("UTF-8");
        StringBuffer sb = new StringBuffer();
        String base64encodedString = Base64.encode(textByte);
        sb.append(base64encodedString).append(randomKey);
        String sbl = String.valueOf(sb);
        String md5 = MD5Util.string2MD5(String.valueOf(sbl));
        JSONObject jsonb = new JSONObject();
        jsonb.put("object", base64encodedString);
        jsonb.put("sign", md5);
        String connectValue = jsonb.toJSONString();
        String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
        try {
            System.out.println(token+"token");
            System.out.println("randomKey"+randomKey);
            System.out.println(connectValue+"connectValue");
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/unit/addUnit",token, connectValue);
            logger.info("---- return addUnit--responseStr--"+responseStr);
            //responseStr = HttpClientUtil.sendPost("http://101.132.159.222:13010/unit/addUnit",token, connectValue);
            //System.out.println(responseStr);
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            //System.out.println(jsonObject+"单位id");
            responseStr=jsonObject.get("id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("addCompanyId"+e.getMessage());
        }
        return responseStr;

    }
    public String getReportSubmitConnect(Map<String,Object> map,String randomKey, String token){
        logger.info("getReportSubmitConnect 参数:"+map);
        String responseStr="";
        try{
            JSONArray array=new JSONArray();
            JSONObject json = new JSONObject();
            ConnectUtil connectUtil=new ConnectUtil();
            json.put("taskSource", MapUtils.getLong(map,"taskSource"));
            json.put("eatabletype2Id",MapUtils.getInteger(map,"eatableclass2Id"));
            json.put("checkType",MapUtils.getLong(map,"checkType"));
            json.put("checkphase",MapUtils.getString(map,"checkPhase"));
            json.put("wtcompanyId",MapUtils.getLong(map,"wtcompany_id"));
            json.put("codeNo",MapUtils.getString(map,"codeNo"));
            json.put("sampleName",MapUtils.getString(map,"sampleName"));
            json.put("brand",MapUtils.getString(map,"brand"));
            json.put("samplePack",MapUtils.getString(map,"samplePack"));
            json.put("spec",MapUtils.getString(map,"spec"));
            json.put("qualityGrade",MapUtils.getString(map,"qualityGrade"));
            json.put("producerId",MapUtils.getString(map,"producer_id"));
            json.put("manufactureTime", MapUtils.getString(map,"manufactureTime"));
            json.put("manufactureNo",MapUtils.getString(map,"manufactureNo"));
            json.put("sjcompanyId",MapUtils.getLong(map,"sjcompany_id"));
            json.put("sojcompanyId",MapUtils.getLong(map,"sojcompany_id"));
            json.put("value",MapUtils.getString(map,"sampleMethod"));
            json.put("sampleBase",MapUtils.getString(map,"sampleBase"));
            json.put("senderName",MapUtils.getString(map,"senderName"));
            json.put("sampleCount",MapUtils.getString(map,"senderName"));
            json.put("samplePos",MapUtils.getString(map,"samplePos"));
            json.put("sendTime",MapUtils.getString(map,"sendTime"));
            json.put("bcheckRemark",MapUtils.getString(map,"bcheckRemark"));
            json.put("sampleTime",MapUtils.getString(map,"sendTime"));
            json.put("jcdepartmentId",85);
            json.put("checkTime",MapUtils.getString(map,"checkTime"));
            json.put("checkTimeEnd",MapUtils.getString(map,"checkTimeEnd"));
            json.put("checkItems",MapUtils.getString(map,"checkItems"));
            json.put("checkResult",MapUtils.getString(map,"checkResult"));
            json.put("checkAccording",MapUtils.getString(map,"checkAccording"));
            json.put("checkPos",MapUtils.getString(map,"samplePos"));
            json.put("instrument",MapUtils.getString(map,"instrument"));
            json.put("environment",MapUtils.getString(map,"environment"));
            json.put("resultRemark",MapUtils.getString(map,"resultRemark"));
            json.put("signTime",MapUtils.getString(map,"signTime"));
            json.put("remark",MapUtils.getString(map,"remark"));
            json.put("makerName",MapUtils.getString(map,"makerName"));
            json.put("approverName",MapUtils.getString(map,"approverName"));
            json.put("assessorName",MapUtils.getString(map,"assessorName"));
            json.put("creditCode",MapUtils.getString(map,"creditCode"));
            json.put("naturalPersonId",MapUtils.getString(map,"naturalPersonId"));
            json.put("belongReportPlan","");
            json.put("wfState",3);
            JSONArray jsonArray=new JSONArray();
            List<Map<String,Object>> checkItemList= (List<Map<String, Object>>) map.get("checkItemList");
            logger.info("checkItemList:"+checkItemList);
            for(Map<String,Object> data:checkItemList){
                String checkResult= StringUtil.safeToString(data.get("checkResult"));
                String name=StringUtil.safeToString(data.get("name"));
                String factorId= null;
                try {
                    factorId = connectUtil.getConnFactorId(name,randomKey,token);
                    logger.info("factorId:"+factorId);
                } catch (UnsupportedEncodingException e) {
                    logger.info("getfactorId:"+e.getMessage());
                    e.printStackTrace();
                }
                try {
                    JSONObject jo=new JSONObject();
                    jo.put("checkResult",checkResult);
                    jo.put("factorId",factorId);
                    jo.put("itemResult",MapUtils.getString(data,"itemResult"));
                    jo.put("name",name);
                    jo.put("remark",MapUtils.getString(data,"remark"));
                    String company=MapUtils.getString(data,"stunit");
                    Map<String,Object> map1=new HashMap<>();
                    jo.put("stname",MapUtils.getString(data,"stname"));
                    map1.put("factorId",factorId);
                    map1.put("company",company);
                    String factorCompany="";
                    //调用市局的检测单位
                    if(factorId!=""){
                        factorCompany = getFactorCompany(randomKey, token, map1);
                    }
                    jo.put("stunit",factorCompany);
                    jo.put("techTarget",MapUtils.getString(data,"techTarget"));
                    jsonArray.add(jo);
                    json.put("checkitemjsonArray",jsonArray);
                    logger.info("checkitemjsonArray"+jsonArray);
                }catch (Throwable ex){
                    logger.info("checkitemjsonArray222"+ex.getMessage());
                    throw new ExceptionInInitializerError(ex);
                }

            }
            logger.info("---checkitemjsonArray---"+jsonArray);
            String jsonvalue=json.toJSONString();
            logger.info("---report data ---"+jsonvalue);
            System.out.println("---report data ---"+jsonvalue);
            byte[] textByte = jsonvalue.getBytes("UTF-8");
            StringBuffer sb = new StringBuffer();
            String base64encodedString = Base64.encode(textByte);
            sb.append(base64encodedString).append(randomKey);
            String sbl=String.valueOf(sb);
            String sbreplace=sbl.replace("\r\n", "");
            String md5=MD5Util.string2MD5(String.valueOf(sbl));
            JSONObject jb=new JSONObject();
            jb.put("object",base64encodedString);
            jb.put("sign",md5);
            String connectValue=jb.toJSONString();
            String sb1=connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
            try {
                System.out.println("reportdata sign object"+connectValue);
                //responseStr = HttpClientUtil.sendPost("http://fsi-api.govsz.com/inputReport/saveReport", token,connectValue);
                responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/inputReport/saveReport", token,connectValue);
                logger.info("---saveReport-result--"+responseStr);

                //JSONObject jsonObject=JSONObject.parseObject(responseStr);
                //String responseStr
            } catch (IOException e) {
                logger.info("getReportSubmitConnect"+e.getMessage());
                e.printStackTrace();

            }
        }catch (Throwable ex){
            StringWriter stringWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(stringWriter));
            logger.info("上报接口报错=="+stringWriter.toString());
            //logger.info("上报接口报错"+ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
        logger.info("---getReportSubmitConnect-报告上传-resukt--"+responseStr);
        return responseStr ;
    }
    //public String getnewEabtableClassinfo(String url,Map<String,String> map,String charset,int outtimes){
    //    JSONObject jsonObject=new JSONObject();
    //    String result3= null;
    //    try {
    //        result3 = HttpClientUtil.sendGet(url,map,charset,outtimes);
    //        jsonObject = JSONObject.parseObject(result3);
    //        System.out.println(jsonObject+"test");
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return result3;
    //}
    public String getFactorCompany(String randomKey,String token,Map<String,Object> map) throws UnsupportedEncodingException{
        String responseStr = "";
        String result="";
        JSONObject json=new JSONObject();
        json.put("factorId",Integer.parseInt(StringUtil.safeToString(map.get("factorId"))));
        json.put("company",StringUtil.safeToString(map.get("company")));
        JSONObject jo=new JSONObject();
        jo.put("current",1);
        jo.put("size",21);
        json.put("page",jo);
        String jsonvalue = json.toJSONString();
        logger.info("FactorCompany---data----"+jsonvalue);
        byte[] textByte = jsonvalue.getBytes("utf-8");
        StringBuffer sb = new StringBuffer();
        String base64encodedString = Base64.encode(textByte);
        sb.append(base64encodedString).append(randomKey);
        String sbl = String.valueOf(sb);
        String sbreplace = sbl.replace("\r\n", "");
        String  md5 = MD5Util.string2MD5(String.valueOf(sbreplace));
        JSONObject jb = new JSONObject();
        jb.put("object", base64encodedString);
        jb.put("sign", md5);
        String connectValue = jb.toJSONString();
        String sb1 = connectValue.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "");
        try {
            System.out.println(connectValue+connectValue);
            //测试环境
//            responseStr = HttpClientUtil.sendPost("http://47.111.28.93:13021/factorClass/getFactorCompany",token, connectValue);
            //正式环境
            responseStr = HttpClientUtil.sendPost("http://fsi.nbaic.gov.cn:9001/factorClass/getFactorCompany",token, connectValue);
            logger.info("--------return getFactorCompany message---------"+responseStr);
            System.out.println(responseStr);
            if(responseStr!=""&&responseStr!=null) {
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getString("state").equals("true")){
                    result=jsonObject.getJSONObject("result").getJSONArray("records").getJSONObject(0).getString("company");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static java.sql.Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }


    public static void main(String[] args) {
       String ss = "CX07087825555018";
       for(int i =1 ; i< 100; i++){
           String s = ss + String.valueOf(i);
           System.out.println(s);
        }
    }
}
