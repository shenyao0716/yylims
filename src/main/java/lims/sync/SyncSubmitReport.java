package lims.sync;
import com.alibaba.fastjson.JSONObject;
import drp.commons.util.StringUtil;
import lims.service.ReportService;
import lims.util.ConnectUtil;
import lims.util.ImeIntegrationUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import java.text.SimpleDateFormat;
import java.util.*;


public class SyncSubmitReport {
	private static Logger logger = Logger.getLogger(SyncSubmitReport.class);
	private Integer published_collection = 6;//公布通过
	private  static Integer SJ_REPORT_WF_STATE_FINISH=5;///**已批准*/
	public  static Integer SJ_REPORT_SYNCSTATE_SYNC_FAILURE = 3;//同步失败
	private static String spjcgxLoginId="zhuby";
	private static String spjcgxPassword="841917";
	//private static String spjcgxLoginId="zhuboying";
	//private static String spjcgxPassword="111111";
	private StringBuilder sb = new StringBuilder();
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
			logger.info("用户登录开始---报告录入");
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

	private void process(String ramdkey,String token) throws  Exception {
		logger.info("-------提交报告开始执行--------");
		Session hsession=ImeIntegrationUtils.getSession();
		hsession.getTransaction().begin();		//Long submitType = Long.parseLong(Extension.getProperty("submitType"));
		Long submitType= Long.valueOf(5);
		if(null == submitType || submitType != 5)
			return;
		//Integer syncSubmitReport_no = Integer.valueOf(Extension.getProperty("syncSubmitReport_no"));
		Integer syncSubmitReport_no=50;
		logger.info("submitType:" + submitType);
		logger.info("syncSubmitReport_no:" + syncSubmitReport_no);
		if(submitType == null || submitType.intValue() != 5){
			return;
		}
		try{
			ReportService reportService=new ReportService();
			Map<Object,Object> errorMap = new HashMap<Object, Object>();
			//查询本地实验室系统中的SJ_Report表 已批准 未同步 的报告数据
			List<Map<String, Object>> reportListTemp = new ArrayList<Map<String,Object>>();
			reportListTemp=reportService.getNoSyncReportList(hsession);
			//ctx = EntityManagerUtil.currentContext();
			//EntityManager em = ctx.getEntityManager();
			logger.info("reportList数量:" + reportListTemp.size());
			if(reportListTemp.isEmpty())
				return;
			for(Map<String,Object> data :reportListTemp) {
				ConnectUtil connectUtil = new ConnectUtil();
				//Map<Object, Object> reportMap = new HashMap<Object, Object>();
				logger.info("data----"+data);
				logger.info("taskSource"+data.get("taskSource"));
				logger.info("eatableType_id"+data.get("eatableType_id"));
				logger.info("belongReportname"+data.get("belongReportname"));
				logger.info("wtcompanyArea_id"+data.get("wtcompanyArea_id"));
				logger.info("sjcompanyArea_id"+data.get("sjcompanyArea_id"));
				logger.info("producerArea_id"+data.get("producerArea_id"));
				logger.info("checklistitem"+Long.parseLong(StringUtil.safeToString(data.get("id"))));
				if (data.get("taskSource") != null) {
					System.out.println(data.get("taskSource")+"test");
					int taskSource = Integer.parseInt(StringUtil.safeToString(data.get("taskSource")));
					data.put("taskSource", taskSource);
				} else {
					errorMap.put(data.get("id"), "任务来源为空");
				}
				if(data.get("Factor_id") != null){
					int eatableclass2Id=Integer.parseInt(StringUtil.safeToString(data.get("Factor_id")));
					data.put("eatableclass2Id",eatableclass2Id);
				}
				else{
					if (data.get("eatableType_id") != null) {
						long id = Long.parseLong(StringUtil.safeToString(data.get("eatableType_id")));
						System.out.println(data.get("eatableType_id")+"test11");
						Map<String, Object> eatable_map = reportService.getEatableClassById(hsession, id);
						if (eatable_map != null) {//产品类别
							if (eatable_map.get("platform_id") != null) {
								String name = StringUtil.safeToString(eatable_map.get("name"));
								String specialType = StringUtil.safeToString(data.get("specialType_label"));
								Map<String, Object> newEatableClass = reportService.getoldLevelName(hsession, name, specialType);
								String serachproductId = "";
								if (newEatableClass != null) {
									String newfifname = StringUtil.safeToString(newEatableClass.get("newfifthLevelName"));
									if (!"".equals(newfifname)) {
										serachproductId = connectUtil.getConnproductId(newfifname, 5, ramdkey, token);
									} else {
										String newfourthname = StringUtil.safeToString(newEatableClass.get("newFourthLevelName"));
										if (!"".equals(newfourthname)) {
											serachproductId = connectUtil.getConnproductId(newfourthname, 4, ramdkey, token);
										} else {
											String newthirdthname = StringUtil.safeToString(newEatableClass.get("newThirdlevelName"));
											if (!"".equals(newthirdthname)) {
												serachproductId = connectUtil.getConnproductId(newthirdthname, 3, ramdkey, token);
											} else {
												String newsecondname = StringUtil.safeToString(newEatableClass.get("newSecondlevelName"));
												if (!"".equals(newsecondname)) {
													serachproductId = connectUtil.getConnproductId(newsecondname, 2, ramdkey, token);
												} else {
													String firstname = StringUtil.safeToString(newEatableClass.get("newFirstlevelName"));
													if (!"".equals(firstname)) {
														serachproductId = connectUtil.getConnproductId(firstname, 1, ramdkey, token);
													} else {
														errorMap.put(data.get("id"), "映射表无产品类别Id");
													}
												}
											}
										}
									}
									if (!"".equals(serachproductId)) {
										data.put("eatableclass2Id", serachproductId);
									}
									if (serachproductId=="") {
										errorMap.put(data.get("id"), "产品类别跟平台产品类别未同步");
									}
								}
								if (newEatableClass == null) {
									long reportId = Long.parseLong(StringUtil.safeToString(data.get("id")));
									String result = "产品类别映射表无对应数据";
									int status = reportService.updateReportSyncStatefailed(hsession, result, reportId);
								}
								//} else {
								//	errorMap.put(data.get("id"), "产品类别跟平台产品类别未同步");
								//	continue;
								//}
							}
						}else {
							errorMap.put(data.get("id"), "产品类别不存在");
							continue;
						}
					} else {
						errorMap.put(data.get("id"), "请输入产品类别");
						continue;
					}
				}

				//if (data.get("belongReportname") != null) {
				//	String belongReportname=StringUtil.safeToString(data.get("belongReportname"));
				//	logger.info(belongReportname);
				//	String planNum=connectUtil.getConnPlanId(belongReportname,ramdkey,token);
				//	long planNumId=Long.parseLong(planNum);
				//	if(planNum!=null){
				//		reportMap.put("belongReportPlan",planNumId);
				//	}
				//	else {
				//		errorMap.put(data.get("id"), "计划项目名称id查询不到");
				//	}
				//}
				if(data.get("wtcompany_id") != null){
					logger.info("---wtcompany_id---"+data.get("wtcompany_id"));
					String companyname =StringUtil.safeToString(data.get("wtcompany_label"));
					String resultproductId=connectUtil.getConnCompanyId(companyname,ramdkey,token);
					logger.info("==return resultproductId=="+resultproductId);
					if(resultproductId==""){
						Map<String,Object> map=reportService.getcompanyinfo(hsession,Long.parseLong(StringUtil.safeToString(data.get("wtcompany_id"))));
						logger.info("---wtcompany map---"+map);
						String wtcompany_id=connectUtil.addCompanyId(map,ramdkey,token,1);
						logger.info("===return  wtcompany_id==="+wtcompany_id);
						data.remove("wtcompany_id");
						data.put("wtcompany_id",wtcompany_id);
					}
					else {
						data.remove("wtcompany_id");
						data.put("wtcompany_id",resultproductId);
					}
				}
				if(data.get("sjcompany_id") != null){
					logger.info("---sjcompany_id---"+data.get("sjcompany_id"));
					String companyname =StringUtil.safeToString(data.get("sjcompany_label"));
					String resultproductId=connectUtil.getConnCompanyId(companyname,ramdkey,token);
					logger.info("==return resultproductId=="+resultproductId);
					if(resultproductId==""){
						Map<String,Object> map=reportService.getcompanyinfo(hsession,Long.parseLong(StringUtil.safeToString(data.get("sjcompany_id"))));
						logger.info("---sjcompany map---"+map);
						String sojcompany_id=connectUtil.addCompanyId(map,ramdkey,token,2);
						logger.info("===return  sojcompany_id==="+sojcompany_id);
						data.remove("sjcompany_id");
						data.put("sjcompany_id",sojcompany_id);
					}
					else {
						data.remove("sjcompany_id");
						data.put("sjcompany_id",resultproductId);
					}
				}
				if(data.get("producer_id") != null){
					logger.info("---producer_id---"+data.get("producer_id"));
					String companyname =StringUtil.safeToString(data.get("producer_label"));
					String resultproductId=connectUtil.getConnCompanyId(companyname,ramdkey,token);
					logger.info("==return resultproductId=="+resultproductId);
					if(resultproductId==""){
						Map<String,Object> map=reportService.getcompanyinfo(hsession,Long.parseLong(StringUtil.safeToString(data.get("producer_id"))));
						logger.info("---producer map---"+map);
						String producer_id=connectUtil.addCompanyId(map,ramdkey,token,5);
						logger.info("===return  sojcompany_id==="+producer_id);
						data.remove("producer_id");
						data.put("producer_id",producer_id);
					}
					else {
						data.remove("producer_id");
						data.put("producer_id",resultproductId);
					}
				}
				if(data.get("sojcompany_id") != null){
					logger.info("---sojcompany_id---"+data.get("sojcompany_id"));
					String companyname =StringUtil.safeToString(data.get("sojcompany_label"));
					String resultproductId=connectUtil.getConnCompanyId(companyname,ramdkey,token);
					logger.info("====resultproductId===="+resultproductId);
					if(resultproductId==""){
						Map<String,Object> map=reportService.getcompanyinfo(hsession,Long.parseLong(StringUtil.safeToString(data.get("sojcompany_id"))));
						logger.info("---sojcompany map---"+map);
						String sojcompany_id=connectUtil.addCompanyId(map,ramdkey,token,6);
						logger.info("---sojcompany_id ---"+sojcompany_id);
						data.remove("sojcompany_id");
						data.put("sojcompany_id",sojcompany_id);
					}
					else {
						data.remove("sojcompany_id");
						data.put("sojcompany_id",resultproductId);
					}
				}
				List<Map<String,Object>> getcheckItemList=reportService.getCheckItemlist(hsession,Long.parseLong(StringUtil.safeToString(data.get("id"))));
				System.out.println(getcheckItemList+"testgetcheckItem");
				List<Map<String,Object>> itemlist=new ArrayList<Map<String, Object>> ();
				if(getcheckItemList.size()>0) {
					for (Map<String, Object> checkmap : getcheckItemList) {
						String checkItem = checkmap.get ("name").toString ();
						Map<String, Object> itemClass = reportService.getItemClass (hsession, checkItem);
						if (itemClass != null) {
							String cityItemName = itemClass.get ("cityItemName").toString ();
							if(cityItemName!=null){
								Map<String,Object> CityClass=reportService.getCityItemClass (hsession,cityItemName);
								if(CityClass!=null){
									String Cityname=CityClass.get ("name").toString ();
									checkmap.put ("name", Cityname);
								}
							}
						}
						itemlist.add (checkmap);
					}
					data.put ("checkItemList", getcheckItemList);
				}
				else {
					errorMap.put(data.get("id"),"无检测项目");
				}
				if (errorMap.isEmpty()){
					String  result=connectUtil.getReportSubmitConnect(data,ramdkey,token);
					System.out.println("报告的结果"+result);
					long reportId=Long.parseLong(StringUtil.safeToString(data.get("id")));
					try {
						//更新报告状态
						if(result.equals("保存录入成功")){

							int status=reportService.updateReportSyncStateSuccess(hsession,reportId);
						}
						else {

							int status=reportService.updateReportSyncStatefailed(hsession,result,reportId);
						}
					}catch (Exception e){
						e.printStackTrace();
						hsession.getTransaction().rollback();
					}
				}
				errorMap.clear();
				Thread.sleep(1000);
			}

		}catch (Exception e){
			e.printStackTrace();
			hsession.getTransaction().rollback();
		}
		finally {
			ImeIntegrationUtils.closeSession();
		}
	}
}