package lims.util;


import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xinghl on 2017/10/18.
 */
public class HttpClientUtil {
    public static String sendPost(String url,String Params)throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        try {

            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(60000);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
            throw e;

        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
    public static String sendPost(String url,String authToken,String Params)throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        StringBuffer sb=new StringBuffer();
        sb.append("Bearer").append(" ").append(authToken);
        String auth=String.valueOf(sb);
        try {

            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Authorization",auth);
            //conn.setRequestProperty("Authorization" ,auth);
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(60000);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
            throw e;

        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
    public static String sendPost1(String url,String authToken,String Params)throws IOException {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response="";
        StringBuffer sb=new StringBuffer();
        sb.append("Bearer").append(" ").append(authToken);
        String auth=String.valueOf(sb);
        try {

            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Authorization",auth);
            //conn.setRequestProperty("Authorization" ,auth);
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(60000);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
            throw e;

        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return response;
    }
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param arcParam
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, String> arcParam, String charset, int times) throws SocketTimeoutException, Exception {
        String result = "";
        BufferedReader in = null;
        String token="Bearer"+" "+arcParam.get("token");
        try {
            StringBuilder sb = new StringBuilder();
            StringBuffer sbRet = new StringBuffer();
            sb.append(url).append("?");
            boolean isStart = true;
            Iterator getMethod = arcParam.entrySet().iterator();

            while(getMethod.hasNext()) {
                Map.Entry client = (Map.Entry)getMethod.next();
                if(isStart) {
                    sb.append((String)client.getKey()).append("=").append(  URLEncoder.encode((String)client.getValue(),"utf8"));
                    isStart = false;
                } else {
                    sb.append("&").append((String)client.getKey()).append("=").append(URLEncoder.encode((String)client.getValue(),"utf8"));
                }
            }
            URL realUrl = new URL(sb.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(times);
            connection.setReadTimeout(times);
            connection.setRequestProperty("Authorization",token);
            // 建立实际的连接
            connection.connect();

            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), charset));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (SocketTimeoutException ex){
            ex.printStackTrace();
            throw new SocketTimeoutException("链接超时");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        //String s = "[{\"cityno\":\"330200\",\"shopyytime\":\"\",\"shopCimgStatus\":90,\"origin\":1,\"shopaddress\":\"宁波市江东区曙光路553号\",\"yxl\":0,\"shopstate\":0,\"platform\":3,\"shopBimgStatus\":90,\"shopBimg\":\"http://p1.meituan.net/xianfu/5c967c9a8396170aa4495219725ccdd591533.jpg\",\"shopCimg\":\"http://p1.meituan.net/xianfu/c762a7eed24d0ea208402620d8fa0f9d108231.png\",\"xkzh\":\"\",\"shopid\":\"69745c9770b3ff38e80d602394e40d9c\",\"id\":11843,\"shopphone\":\"\"}]\n";
        //String s1 = "[{\"userName\":\"zhuboying\",\"password\":\"111111\"}]";
//        String var1="zhuby";
//        String var2="841917";
//        //String var1="zhuboying";
//        //String var2="111111";
//        ConnectUtil connectUtil=new ConnectUtil();
//        String result=connectUtil.getloginConnect(var1,var2);
//        JSONObject rs=JSONObject.parseObject(result);
//        System.out.println("rs"+rs);
//        String key= rs.get("randomKey").toString();
//        String taken=rs.get("token").toString();
//        System.out.println(key+"key");
//        System.out.println("taken"+taken);
//        //Date beginDate=new Date();
//        //Date endDate=new Date();
//        //Integer state=4;
//        String var3="螨";
//        //String start="2017-08-01";
//        //String end="2017-08-31";
//        Map<String,Object> map=new HashMap<>();
//        String result3=connectUtil.getConnFactorId(var3,key,taken);
//        System.out.println(result3+"result3");
//        map.put("factorId",result3);
//        if(result3!=""){
//            String result4=connectUtil.getFactorCompany(key,taken,map);
//            System.out.println(result4+"85123");
//        }

//        System.out.println(result4);
//        System.out.println(result+result);
          String result=",";
          if(result.indexOf("3")!=-1){
              System.out.println("1232323");
          }else{
              System.out.println("44444444");
          }
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
}
