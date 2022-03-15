package com.zaimus.Profiles;

public class Profile {
    public String customer_id;
    private String customer_name;
    private String customer_category;
    private String customer_add1;
    private String customer_add2;
    private String customer_add3;
    private String customer_add4;
    private String customer_add5;
    private String customer_phone;
    private String customer_place;
    private String customer_region;
    private String customer_zone;
    private String customer_businesstype;
    private String customer_dist;
    private String customer_shopname;
    private String customer_phone2;
    private String customer_email;
    private String customer_status;
    private String customer_state;
    private String customer_desg;
    private String customer_country;
    private String customer_department;
    private String latitude;
    private String longitude;
    private String zaimus_id;
    private String isVerified;
    private String isMarkedDeletion;
    private String relevance;

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String getCustomer_country() {
        return customer_country;
    }

    public void setCustomer_country(String customer_country) {
        this.customer_country = customer_country;
    }

    public String getCustomer_department() {
        return customer_department;
    }

    public void setCustomer_department(String customer_department) {
        this.customer_department = customer_department;
    }

    public String get_customer_id() {
        return customer_id;
    }

    public String get_customer_desg() {
        return customer_desg;
    }

    public String get_customer_status() {
        return customer_status;
    }

    public String get_customer_name() {
        return customer_name;
    }

    public String get_customer_category() {
        return customer_category;
    }

    public String get_customer_add1() {
        return customer_add1;
    }

    public String get_customer_add2() {
        return customer_add2;
    }

    public String get_customer_add3() {
        return customer_add3;
    }

    public String get_customer_add4() {
        return customer_add4;
    }

    public String get_customer_add5() {
        return customer_add5;
    }

    public String get_customer_phone() {
        return customer_phone;
    }

    public String get_customer_place() {
        return customer_place;
    }

    public String get_customer_region() {
        return customer_region;
    }

    public String get_customer_zone() {
        return customer_zone;
    }

    public String get_customer_businesstype() {
        return customer_businesstype;
    }

    public String get_customer_dist() {
        return customer_dist;
    }

    public String get_customer_shopname() {
        return customer_shopname;
    }

    public String get_customer_phone2() {
        return customer_phone2;
    }

    public String get_customer_email() {
        return customer_email;
    }

    public String get_customer_state() {
        return customer_state;
    }

    public String get_customer_lat() {
        return latitude;
    }

    public String get_isVerified() {
        return isVerified;
    }

    public String get_isMarkedforDeletion() {
        return isMarkedDeletion;
    }

    public String get_customer_lng() {
        return longitude;
    }

    public String getZaimus_id() {
        return zaimus_id;
    }

    public void setZaimus_id(String zaimus_id) {
        this.zaimus_id = zaimus_id;
    }

    public void setProfile(String customer_id, String customer_name,
                           String customer_category, String customer_add1,
                           String customer_add2, String customer_add3, String customer_add4,
                           String customer_add5, String customer_phone, String customer_place,
                           String customer_region, String customer_zone,
                           String customer_businesstype, String customer_dist,
                           String customer_shopname, String customer_phone2,
                           String customer_email, String customer_status,
                           String customer_state, String customer_desg,
                           String customer_country, String customer_department,
                           String latitude, String longitude, String isVerified, String isMarkedDeletion, String relevance, String zaimus_id) {

        // //System.out.println(customer_country + "setProfile customer_country"
        // +"customer_department" +customer_department );

        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_category = customer_category;
        this.customer_add1 = customer_add1;
        this.customer_add2 = customer_add2;
        this.customer_add3 = customer_add3;
        this.customer_add4 = customer_add4;
        this.customer_add5 = customer_add5;
        this.customer_phone = customer_phone;
        this.customer_place = customer_place;
        this.customer_region = customer_region;
        this.customer_region = customer_region;
        this.customer_zone = customer_zone;
        this.customer_businesstype = customer_businesstype;
        this.customer_dist = customer_dist;
        this.customer_shopname = customer_shopname;
        this.customer_phone2 = customer_phone2;
        this.customer_email = customer_email;
        this.customer_status = customer_status;
        this.customer_state = customer_state;
        this.customer_desg = customer_desg;
        this.customer_country = customer_country;
        this.customer_department = customer_department;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isMarkedDeletion = isMarkedDeletion;
        this.isVerified = isVerified;
        this.relevance = relevance;
        this.zaimus_id = zaimus_id;
    }

    /*
     * public void setProfile(String customer_id, String customer_name, String
     * customer_category, String customer_add1, String customer_add2, String
     * customer_add3, String customer_add4, String customer_add5, String
     * customer_phone, String customer_place, String customer_businesstype,
     * String customer_dist, String customer_shopname, String customer_phone2,
     * String customer_email, String customer_status, String customer_state,
     * String customer_desg,String customer_country,String customer_department)
     * {
     *
     * //System.out.println(customer_country + "setProfile customer_country"
     * +"customer_department" +customer_department );
     *
     * this.customer_id = customer_id; this.customer_name = customer_name;
     * this.customer_category = customer_category; this.customer_add1 =
     * customer_add1; this.customer_add2 = customer_add2; this.customer_add3 =
     * customer_add3; this.customer_add4 = customer_add4; this.customer_add5 =
     * customer_add5; this.customer_phone = customer_phone; this.customer_place
     * = customer_place; this.customer_businesstype = customer_businesstype;
     * this.customer_dist = customer_dist; this.customer_shopname =
     * customer_shopname; this.customer_phone2 = customer_phone2;
     * this.customer_email = customer_email; this.customer_status =
     * customer_status; this.customer_state = customer_state; this.customer_desg
     * = customer_desg; this.customer_country = customer_country;
     * this.customer_department = customer_department ; }
     */

}
