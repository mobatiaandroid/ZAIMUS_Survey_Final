package com.zaimus.api;


public interface IVkcApis {

    // dev links
    //String DOMAIN_VKC = "http://dev.mobatia.com/vkcsurveyV2/api/";
	/*String DOMAIN_VKC = "http://dev.mobatia.com/vkcsurveyV2/api/";
    String IMAGE_FEEDBACK_URL = "http://dev.mobatia.com/vkcsurveyV2/";
	String DOMAIN_IMAGE_UPLOAD = "http://dev.mobatia.com/vkcsurveyV2/api/upload";*/
    /*
     * Dev Test Account
     *
     * mahesh vkc1234
     */

  
    ///  Dev
    String DOMAIN_VKC = "http://dev.mobatia.com/zaimus_survey/api/";
    String
            IMAGE_FEEDBACK_URL = "http://dev.mobatia.com/zaimus_survey";
    String
            DOMAIN_IMAGE_UPLOAD = "http://dev.mobatia.com/zaimus_survey/api/upload";
// Live


 /*String DOMAIN_VKC = "http://ec2-23-22-108-196.compute-1.amazonaws.com/zaimus_survey/api/";
 String
         IMAGE_FEEDBACK_URL = "http://ec2-23-22-108-196.compute-1.amazonaws.com/zaimus_survey/";
 String
         DOMAIN_IMAGE_UPLOAD = "http://ec2-23-22-108-196.compute-1.amazonaws.com/zaimus_survey/api/upload";
*/


    /*String DOMAIN_VKC = "http://192.168.0.177/Zaimus/Production/zaimus_survey/api/";
     String
             IMAGE_FEEDBACK_URL = "http://192.168.0.177/Zaimus/Production/zaimus_survey/";
     String
             DOMAIN_IMAGE_UPLOAD = "http://192.168.0.177/Zaimus/Production/zaimus_survey/api/upload";
 */
    String API_ACTION_DBDUMP = "get_dbdump";
    String API_ACTION_GPSSAVE = "savegpslog";
    String API_ACTION_SYNCCUSTOMERS = "migrateCustomer";
    String API_ACTION_SAVE_SERVEY = "save_survey";
    String API_ACTION_SET_CUSTOMER = "set_customer";
    String API_ACTION_SUBMIT_CLAIMS = "claims";
    String API_ACTION_GET_CLAIMSLIST = "myClaims";
    String API_ACTION_GET_PLANLIST = "gettourplan";
    String API_ACTION_GET_PLANLISTDETAIL = "gettourplandetails";
    String API_ACTION_GETBALANCE = "getplanbalance";
    String API_ACTION_LOGIN = "login";
    String API_ACTION_ATTENDANCE = "attendance";
    int CUSTOMER_LIST = 0;
    int SERVEY_LIST = 1;
    String API_ACTION_SAVE_GPS_CUSTOMER = "saveCustomerGps";
    public String surveyType = "sales";
    public String surveySetZoneName = "zone";
    public String surveySeyZoneValue = "1";
    // 1 -TamilNadu, 3- Karnadaka, 5-
    // Andrapradesh, 7- Gujarat
}
