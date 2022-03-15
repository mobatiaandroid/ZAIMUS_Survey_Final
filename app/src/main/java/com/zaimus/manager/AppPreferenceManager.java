package com.zaimus.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferenceManager {

    public static void saveGpsStatus(boolean status, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("gps_status", status).commit();
    }

    public static boolean getGpsStatus(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        boolean status = preferences.getBoolean("gps_status", false);
        return status;

    }

    public static void saveAttendanceId(String id, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("attendance_id", id).commit();
    }

    public static String getAttendanceId(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String status = preferences.getString("attendance_id", "");
        return status;

    }

    public static void saveAttendance(boolean status, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("attendance", status).commit();
    }

    public static boolean getAttendance(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        boolean status = preferences.getBoolean("attendance", false);
        return status;

    }

    public static void saveUserId(String userId, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("user_id", userId).commit();
    }

    public static String getUserId(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String userId = preferences.getString("user_id", "");
        return userId;
    }

    public static void saveDeviceID(String device_id, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("device_id", device_id).commit();
    }

    public static String getDeviceID(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String device_id = preferences.getString("device_id", "");
        return device_id;
    }



    public static void saveRoleId(String roleId, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("role_id", roleId).commit();
    }

    public static String getRoleId(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String role_id = preferences.getString("role_id", "");
        return role_id;
    }

	/*public static void saveJson(String jsonStr, Context ctx) {
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
		preferences.edit().putString("jsonStr", jsonStr).commit();
	}

	public static String getJson(Context ctx) {
		SharedPreferences preferences;
		preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
		String jsonStr = preferences.getString("jsonStr", "");
		return jsonStr;
	}*/

    public static void saveSurveySetId(String survey_setid, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("survey_setid", survey_setid).commit();

    }

    public static String getSurveySetId(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String survey_setid = preferences.getString("survey_setid", "");
        return survey_setid;
    }

    public static void saveStateName(String survey_statename, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("survey_statename", survey_statename)
                .commit();

    }

    public static String getStateName(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String survey_statename = preferences.getString("survey_statename", "");
        return survey_statename;
    }

    public static void saveImage(String survey_image, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("survey_image", survey_image).commit();

    }

    public static String getImagee(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String survey_image = preferences.getString("survey_image", "");
        return survey_image;
    }

    public static void saveFlag(String flag, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("flag", flag).commit();

    }

    public static String getFlag(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String flag = preferences.getString("flag", "");
        return flag;
    }

    public static void saveUsername(String username, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("username", username).commit();

    }

    public static String getUsername(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        return username;
    }

    public static void savePassword(String password, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("password", password).commit();

    }

    public static String getPassword(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String password = preferences.getString("password", "");
        return password;
    }

    public static void savePosition(String position, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("position", position).commit();

    }

    public static String getPosition(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String position = preferences.getString("position", "");
        return position;
    }

    public static void saveSurveyName(String name, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("Survey Name", name).commit();

    }

    public static String getSurveyName(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String name = preferences.getString("Survey Name", "");
        return name;
    }

    public static void saveSurveyId(String id, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("Survey Id", id).commit();

    }

    public static String getSurveyId(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String id = preferences.getString("Survey Id", "");
        return id;
    }

    public static void saveLocation(String name, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("Location", name).commit();

    }

    public static String getLocation(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String name = preferences.getString("Location", "");
        return name;
    }

    public static void saveSyncStatus(String status, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("SyncStatus", status).commit();

    }

    public static String getSyncStatus(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String status = preferences.getString("SyncStatus", "");
        return status;
    }

    public static void saveUserStatus(String status, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("Status", status).commit();

    }

    public static String getUserStatus(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String status = preferences.getString("Status", "");
        return status;
    }

    public static void savePreviousUser(String status, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("User", status).commit();

    }

    public static String getPreviousUser(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String status = preferences.getString("User", "");
        return status;
    }

    public static void saveClaimImage(String ClaimImage, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("ClaimImage", ClaimImage).commit();

    }

    public static String getClaimImage(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String ClaimImage = preferences.getString("ClaimImage", "");

        return ClaimImage;
    }

    public static void setSurveyNamePreference(String survey_name,
                                               Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("VKC App",
                Context.MODE_PRIVATE);
        preferences.edit().putString("Survey Set Name", survey_name).commit();
    }

    public static String getSurveyNamepreference(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("VKC App",
                Context.MODE_PRIVATE);
        String surveyname = preferences.getString("Survey Set Name", "");
        return surveyname;
    }

    public static void setPlanName(String plan_name, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("VKC App",
                Context.MODE_PRIVATE);
        preferences.edit().putString("Plan Name", plan_name).commit();
    }

    public static String getPlanName(Context context) {
        // //System.out.println("Survey Set ID 123" + surveyset_id);
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("VKC App",
                Context.MODE_PRIVATE);
        String planname = preferences.getString("Plan Name", "");
        return planname;
    }

    public static void setBudget(String budget, Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("VKC App",
                Context.MODE_PRIVATE);
        preferences.edit().putString("Budget", budget).commit();
    }

    public static String getBudget(Context context) {
        SharedPreferences preferences;
        preferences = context.getSharedPreferences("VKC App",
                Context.MODE_PRIVATE);
        String budget = preferences.getString("Budget", "");
        return budget;
    }

    public static void saveDistrict(String dist, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("survey_dist", dist)
                .commit();

    }

    public static String getDistrict(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String survey_dist = preferences.getString("survey_dist",
                "");
        return survey_dist;
    }

    public static void saveListPosition(String cus_id, Context ctx) {
        //System.out.println("Position---->"+cus_id);
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("list position", cus_id)
                .commit();

    }

    public static String getListPosition(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String cus_id = preferences.getString("list position",
                "0");
        //System.out.println("Get Position---->"+cus_id);

        return cus_id;
    }

    public static void saveDate(String survey_image, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("Date", survey_image).commit();

    }

    public static String getDate(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String survey_image = preferences.getString("Date", "");
        return survey_image;
    }

    public static void saveFromPlan(String stat_plan, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("stat_plan", stat_plan).commit();
    }

    public static String getFromPlan(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String stat_plan = preferences.getString("stat_plan", "");
        return stat_plan;
    }

    public static void savePlanDist(String stat_plan, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("plan_dist", stat_plan).commit();
    }

    public static String getPlanDist(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String stat_plan = preferences.getString("plan_dist", "");
        return stat_plan;
    }

    public static void savePlanDrtailId(String PlanDrtailId, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("PlanDrtailId", PlanDrtailId).commit();
    }

    public static String getPlanDrtailId(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String PlanDrtailId = preferences.getString("PlanDrtailId", "");
        return PlanDrtailId;
    }

    public static void saveDistFromPlan(String dist_plan, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("dist_plan", dist_plan).commit();
    }

    public static String getDistFromPlan(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String dist_plan = preferences.getString("dist_plan", "");
        return dist_plan;
    }

    public static void savePreItem(String PreItem, Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        preferences.edit().putString("PreItem", PreItem).commit();
    }

    public static String getPreItem(Context ctx) {
        SharedPreferences preferences;
        preferences = ctx.getSharedPreferences("VKC App", Context.MODE_PRIVATE);
        String PreItem = preferences.getString("PreItem", "");
        return PreItem;
    }
}
