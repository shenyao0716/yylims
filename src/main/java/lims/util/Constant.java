package lims.util;

/**
 *
 *
 *
 */
public class Constant{
	//属性表的的说明
	//查询食品分类
	public static Long Query_EatableClass 	= 1L;
	//查询退回报告
	public static Long Query_Report  		= 2L;
	//查询部门特定分类
	public static Long Query_SpecialClass	= 3L;
	//查询区域
	public static Long Query_Area			= 4L;

	/**超级管理员账户名*/
	public static String ADMINISTRATOR = "超级管理员";

	/**入库*/
	public static long WAREHOUSE_OPERATETYPE_INTO		= 1;
	/**出库*/
	public static long WAREHOUSE_OPERATETYPE_OUT		= 2;
	/**归还*/
	public static long WAREHOUSE_OPERATETYPE_RETURN		= 3;
	/**损*/
	public static long WAREHOUSE_OPERATETYPE_LOSS		= 4;
	/**溢*/
	public static long WAREHOUSE_OPERATETYPE_OVERFLOW	= 5;

	/**未编制*/
	public static long SJ_REPORT_WF_STATE_NOT_PREPARATION					= 1;
	/**编制中*/
	public static long SJ_REPORT_WF_STATE_PREPARATION						= 2;
	/**已编制，待审核*/
	public static long SJ_REPORT_WF_STATE_STAY_AUDITING						= 3;
	/**已审核，待批准*/
	public static long SJ_REPORT_WF_STATE_STAY_FINISH						= 4;
	/**已批准*/
	public static long SJ_REPORT_WF_STATE_FINISH							= 5;
	/**已退回，重新编制*/
	public static long SJ_REPORT_WF_STATE_BACK_AFRESH_PREPARATION			= 6;
	/**已退回,重新审核*/
	public static long SJ_REPORT_WF_STATE_BACK_AFRESH_AUDIT					= 7;
	/**已退回,重新检测*/
	public static long SJ_REPORT_WF_STATE_BACK_AFRESH_CHECK					= 8;

	/**委托受理中*/
	public static long SJ_REPORT_WF_STATE_ACCEPTING							= 21;
	/**未检测*/
	public static long SJ_REPORT_WF_STATE_UN_CHECK							= 22;
	/**已终止*/
	public static long SJ_REPORT_WF_STATE_CANCEL							= 23;

	/**未同步*/
	public static long SJ_REPORT_SYNCSTATE_NOT_SYNC		= 1;
	/**同步*/
	public static long SJ_REPORT_SYNCSTATE_SYNC			= 2;
	/**同步失败*/
	public static long SJ_REPORT_SYNCSTATE_SYNC_FAILURE = 3;
	/**未整理*/
	public static long SJ_REPORT_SYNCSTATE_NOT_CHECK = 4;

	/**政府单位*/
	public static long SJ_RELATEDCOMOANY_COMPANYTYPE_GOVERNMENT = 0;
	/**一般企业*/
	public static long SJ_RELATEDCOMOANY_COMPANYTYPE_COMPANY 	= 1;

	/**任务来源-政府单位*/
	public static long TASKSOURCE_GOVERNMENT 	= 1;
	/**任务来源-一般企业*/
	public static long TASKSOURCE_COMPANY 		= 2;
	/**任务来源-其他来源*/
	public static long TASKSOURCE_OTHER 		= 3;

	/**设备维修*/
	public static long SJ_DEVICELOG_SERVICING				= 1;
	/**报废*/
	public static long SJ_DEVICELOG_REJECT					= 2;
	/**设备启用*/
	public static long SJ_DEVICELOG_ENABLE					= 3;
	/**设备停用*/
	public static long SJ_DEVICELOG_DISABLE					= 4;
	/**期间核查*/
	public static long SJ_DEVICELOG_VERIFICATION			= 5;
	/**校准*/
	public static long SJ_DEVICELOG_CALIBRATION				= 6;

	/**正常*/
	public static long SJ_DEVICE_NORMAL						= 1;
	/**停用*/
	public static long SJ_DEVICE_DISABLE					= 2;
	/**报废*/
	public static long SJ_DEVICE_REJECT						= 3;

	/**样品.操作类型-入库*/
	public static long SAMPLE_OPERATETYPE_INTO			= 4;
	/**样品.操作类型-出库*/
	public static long SAMPLE_OPERATETYPE_OUT			= 3;
	/**样品.操作类型-归还*/
	public static long SAMPLE_OPERATETYPE_RETURN		= 2;
	/**样品.操作类型-领样*/
	public static long SAMPLE_OPERATETYPE_RECEIVE		= 1;

	/**样品.子样品库存状态-在库*/
	public static long SAMPLE_STOCKSTATE_INTO			= 1;
	/**样品.子样品库存状态-已领*/
	public static long SAMPLE_STOCKSTATE_RECEIVE		= 2;
	/**样品.子样品库存状态-销毁/归还客户*/
	public static long SAMPLE_STOCKSTATE_OUT			= 3;
	/**样品.子样品库存状态-已归还*/
	public static long SAMPLE_STOCKSTATE_RETURN			= 4;
	/**样品.子样品库存状态-未入库*/
	public static long SAMPLE_STOCKSTATE_NO				= 9;


	/**样品.样品库存状态-未入库*/
	public static long SAMPLE_WAREHOUSESTATE_NO			= 1;
	/**样品.样品库存状态-已打印，未入库*/
	public static long SAMPLE_WAREHOUSESTATEE_STAMP		= 2;
	/**样品.样品库存状态-已入库*/
	public static long SAMPLE_WAREHOUSESTATE_IN			= 3;
	/**样品.样品库存状态-销毁/归还客户*/
	public static long SAMPLE_WAREHOUSESTATE_RETURN		= 4;

	/**标准.标准状态 正常*/
	public static long STANDARDSTATE_NORMAL				= 1;
	/**标准.标准状态 作废*/
	public static long STANDARDSTATE_INVALID			= 0;

	/**为true的时候委托单样品走检测流程*/
	public static String CHECKMODE						= Extension.getProperty("checkMode");
	public static String CHECKMODE_TRUE					= "true";
	public static String CHECKMODE_FALSE				= "false";

	public static String CODENO_BEGIN					= Extension.getProperty("codeNo_begin");
	public static String CODENO_END						= Extension.getProperty("codeNo_end");
	public static int SYNCTASK_TIME						= Integer.valueOf(Extension.getProperty("syncTask_time")).intValue();
}