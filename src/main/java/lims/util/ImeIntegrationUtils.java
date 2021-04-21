package lims.util;

import org.hibernate.Session;
import xc.core.EntityManagerUtil;

public class ImeIntegrationUtils {
	private static final  boolean ISIME_INTEGRATION=true;
	public static Session getSession(){
		Session session=null;
		if(ISIME_INTEGRATION){
			session=apf.util.EntityManagerUtil.currentContext().getHibernateSession();
		}else{
			session=EntityManagerUtil.currentContext().getHibernateSession();
		}
		return session;
	}

	public static void closeSession() {
		if(ISIME_INTEGRATION){
//			apf.util.EntityManagerUtil.closeSession(null);
		}else{
			EntityManagerUtil.closeSession(null);
		}
		
	}
}
