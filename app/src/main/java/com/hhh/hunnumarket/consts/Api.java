package com.hhh.hunnumarket.consts;

public class Api {
    private final static String BASE_URL_LOCAL = "http://10.0.2.2:8080";
    private final static String BASE_URL_WEB = "http://192.168.16.128:8080";

    public final static String SEPARATOR = "/";
    public final static String BASE_URL = BASE_URL_WEB;

    public final static String SPLASH = "hm/file";
    public final static String LOGIN = BASE_URL+"/hm/api/login";
    public final static String ITEM_IMAGE = BASE_URL+"/hm/";
    public final static String REGISTER = BASE_URL+"/hm/api/register";
    public final static String GET_GOODS = BASE_URL+"/hm/api/getGoods";
    public final static String GET_GOOD_PICS = BASE_URL+"/hm/api/getGoodPics";
    public final static String POST_ITEM = BASE_URL+"/hm/api/postItem";
    public final static String GET_CATEGORIES = BASE_URL+"/hm/api/getCategories";

    public static String getFileUrl(String fileName){
        return BASE_URL+SEPARATOR+SPLASH+SEPARATOR+fileName;
    }

}
