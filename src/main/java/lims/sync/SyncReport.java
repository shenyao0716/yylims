package lims.sync;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import drp.commons.util.StringUtil;
import lims.service.ReportService;
import lims.util.ConnectUtil;
import lims.util.ImeIntegrationUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 向平台查询报告，目前向平台提交报告,提交方式目前分为三种 1:正在录入状态 3:未审核 5:未公布：查询两种状态报告， 4：公布通不过 6：已公布
 *
 */

public class SyncReport {
	private static Logger logger = Logger.getLogger(SyncReport.class);
	//private Integer published_collection = 4;//公布通不过
	private Integer published_collection = 6;//公布通过
	private  static Integer SJ_REPORT_WF_STATE_FINISH=5;///**已批准*/
	public  static Integer SJ_REPORT_SYNCSTATE_SYNC_FAILURE = 3;//同步失败
	//private static String spjcgxLoginId="zhuboying";
	//private static String spjcgxPassword="111111";
    private static String spjcgxLoginId="zhuby";
    private static String spjcgxPassword="841917";
	private static String Query_Report="2"; //查询退回报告
	public void handle() {
		try{
			//String spjcgxLoginId  =   Extension.getProperty("spjcgx.user.loginId");
			//String spjcgxPassword = Extension.getProperty("spjcgx.user.password");
			//String limsLoginId = Extension.getProperty("lims.user.loginId");
			//
			//if(!LoginSession.isLogined()){
			//	LoginSession loginSession = new LoginSession();
			//	loginSession.trustLogin(limsLoginId, "ime.com");
			//}
			logger.info("用户登录开始---报告查询");
			Session hsession=ImeIntegrationUtils.getSession();
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
		logger.info("-------查询平台退回报告执行开始--------");
		Session hsession=ImeIntegrationUtils.getSession();
		hsession.getTransaction().begin();
		Transaction tx = null;
		//Long submitType = Long.parseLong(Extension.getProperty("submitType"));
		Long submitType= Long.valueOf(5);
		if(null == submitType || submitType != 5)
			return;
		//SessionContext ctx = null;
		//EntityTransaction tx = null;
		try {
			//ctx = EntityManagerUtil.currentContext();
			//EntityManager em = ctx.getEntityManager();
			//Query query = em.createQuery("from SJ_Property where name=:name");
			//query.setParameter("name", Constant.Query_Report.toString());
			ReportService reportService=new ReportService();
			List<Map<String, Object>> list = reportService.getPropery(hsession,Query_Report);
			Date beginTime = null;
			Map<String, Object> map = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (!list.isEmpty()){
				map = list.get(0);
				String val = (String) map.get("val");// 取SJ_Property中的val值
				if(val != null)
					beginTime = df.parse(val);
			}
			Date endTime=new Date();
			String date1=df.format(beginTime);
			String date2=df.format(new Date());
			// 查询平台查询报告
			//Long jcdepartment_id = Long.parseLong(Extension.getProperty("spjcgx.jcdepartment_id"));
            ConnectUtil connectUtil=new ConnectUtil();
            logger.info("data1"+date1+"date2"+date2+"ramdkey"+ramdkey+"token"+token);
            JSONArray reportlist=connectUtil.getSerachReportConnect(date1,date2,published_collection,ramdkey,token);
            if(reportlist.size()==0)
            return;
			try{
				 tx= hsession.getTransaction();
				// 修改本地报告的状态为同步失败。
				for(int index = 0; index < reportlist.size(); index++){
					JSONObject jsonObject = ((JSONObject) reportlist.get(index)).getJSONObject("report");
					//query = em.createQuery("select id, syncState, WF_State from SJ_Report where platform_id=:platform_id");
					//query.setParameter("platform_id", jsonObject.getLong("id"));
					long platfromId=jsonObject.getLong("id");
					List<Map<String,Object>> report_list=reportService.getReportByplatformId(hsession,platfromId);
					//List<Object> report_list = query.getResultList();
					if(report_list.size()>0&&report_list!=null){
						Map<String,Object> record=report_list.get(0);
						Integer WF_State = Integer.parseInt(StringUtil.safeToString(record.get("WF_State")));
						//if(WF_State != Constant.SJ_REPORT_WF_STATE_FINISH)
						if(WF_State!=SJ_REPORT_WF_STATE_FINISH)
							return;
					/*
					Map<String,String> entityMap = new HashMap<String, String>();
					entityMap.put("id", CommUtil.getValue(record[0]));
					entityMap.put("WF_State", CommUtil.getValue(Constant.SJ_REPORT_WF_STATE_RETURN));
					entityMap.put("WF_StateName", "公布退回，待编制");
					entityMap.put("submitType", "1");
					entityMap.put("message", jsonObject.getString("message"));
					entityMap.put("syncState", CommUtil.getValue(Constant.SJ_REPORT_SYNCSTATE_SYNC_FAILURE));

					this.modifyReport(entityMap, null);
					*/
						//2013-01-28 增加
						//query = em.createQuery("update SJ_Report set syncState=:syncState, submitType=:submitType, message=:message where id=:id");
						//query.setParameter("syncState", Constant.SJ_REPORT_SYNCSTATE_SYNC_FAILURE);
						//query.setParameter("message", jsonObject.getString("message"));
						//query.setParameter("submitType", 1L);
						//query.setParameter("id", record[0]);
						//query.executeUpdate();
						String message="同步失败";
						//long syncState=Constant.SJ_REPORT_SYNCSTATE_SYNC_FAILURE;
						long syncState=SJ_REPORT_SYNCSTATE_SYNC_FAILURE;
						long id=Long.parseLong(StringUtil.safeToString(record.get("id")));
						int status=reportService.updateReportStatusById(hsession,message,syncState,id);
					}

				}
				// 更新SJ_Property表
				if(!list.isEmpty()){
					Map<String, String> entity = new HashMap<String, String>();
					long id=Long.parseLong(StringUtil.safeToString(map.get("id")));
					String var=df.format(endTime);
					int stauts=reportService.updatePropertyById(hsession,id,var);
				}else{
					long id=Long.parseLong(StringUtil.safeToString(map.get("id")));
					int status=reportService.updatePropertyById(hsession,id);
				}
					tx.commit();
			}catch (Exception e) {
				e.printStackTrace();
					tx.rollback();
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ImeIntegrationUtils.closeSession();
		}

		logger.info("-------查询平台退回报告执行结束--------");
	}
	public static void main(String[] args)  {
		//SyncSubmitReport syncSubmitRepot=new SyncSubmitReport();
		//syncSubmitRepot.handle();
		SyncSubmitReport syncSubmitReport=new SyncSubmitReport();
		syncSubmitReport.handle();
		//Session hsession=ImeIntegrationUtils.getSession();
		//ReportService reportService=new ReportService();

	}
	}
