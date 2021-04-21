package lims.util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xinghl on 2018/5/24.
 */
public class HttpClient {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param arcParam
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendpost(String body, String url){
        org.apache.http.client.HttpClient httpclient=null;
        BasicHttpParams params= new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 30000);
        httpclient=new DefaultHttpClient(params);
        //ApiService api=new DefaultApiServiceImpl();
        //SessionContext context = EntityManagerUtil.currentContext();
        //Session hsession = context.getHibernateSession();
        HttpPost post=new HttpPost(url);
        HttpEntity entity = new StringEntity(body, Charset.forName("utf8"));
        post.addHeader("Connection", "Keep-Alive");
        //entity.setContentEncoding("UTF-8");
        // 发送Json格式的数据请求
        post.addHeader("Content-Type","application/json");
        post.setEntity(entity);

        try{
            HttpResponse execute = httpclient.execute(post);
            String content = EntityUtils.toString(execute.getEntity(),"utf-8");
            return content;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("客户端调用错误");
        }finally{
            post.abort();
            HttpClientUtils.closeQuietly(httpclient);
        }
        //ResultDto validate = api.validateall(,hsession );
        //System.out.println( JSONObject.toJSONString(validate) );
    }

    public static void main(String[] args) throws Exception{
//            String resut = "未检出（检出限1.0）";
//            if(resut.indexOf("（")!=-1){
//                String result=resut.replaceAll("（","(").replaceAll("）",")");
//                    String pattern = "\\([^)]*\\)";	//括号内
////String pattern = "\\(.+"
//                    resut = result.replaceAll(pattern, "");
//                    System.out.println("resut"+resut);
//                }
//            else if(resut.indexOf("(")!=-1){
//                String result=resut.replaceAll("（","(").replaceAll("）",")");
//                String pattern = "\\([^)]*\\)";	//括号内
////String pattern = "\\(.+"
//                resut = result.replaceAll(pattern, "");
//                System.out.println("resut"+resut);
//            }else{
//                System.out.println("test"+resut);
//            }
           String result="经抽样检验，克百威不符合GB 2763-2014 《食品安全国家标准 食品中农药最大残留限量》要求，检验结论为不合格。";
           if(result!=null){
               int begin = result.indexOf("经抽样检验，");
               int end=result.indexOf("检验结论为不合格。");
               String substring = result.substring(6, end);
               //String result1="毒死蜱项目不符合GB 2763-2014 《食品安全国家标准 食品中农药最大残留限量》";
               String itemName="毒死蜱";
               String testMethod="GB 2763-2014 《食品安全国家标准 食品中农药最大残留限量》";
               String head = "经抽样检验，";
               String rump = "要求，检验结论为不合格。";
               String resultRemark=head+ substring+itemName + "不符合" + testMethod + rump;
               System.out.println(resultRemark);
           }


    }

}
