package xyz.goome.eternal.common.message;

/**
 * Created by matrix on 16/3/30.
 */
public class Message {

    public static final int MSG_ACCOUNT_MODULE                          = 0x00010000;
    public static final int MSG_ACCOUNT_LOGIN_REQUEST_C2S               = 0x00010001;
    public static final int MSG_ACCOUNT_LOGIN_RESPONSE_S2C              = 0x00010002;
    public static final int MSG_ACCOUNT_REGIST_REQUEST_C2S              = 0x00010003;
    public static final int MSG_ACCOUNT_REGIST_RESPONSE_S2C             = 0x00010004;

    public static final int MSG_SERVER_MODULE                           = 0x00020000;
    public static final int MSG_SERVER_LIST_REQUEST_C2S                 = 0x00020001;
    public static final int MSG_SERVER_LIST_RESPONSE_S2C                = 0x00020002;

    public static final int MSG_ROLE_MODULE                             = 0x00030000;
    public static final int MSG_ROLE_LIST_REQUEST_C2S                   = 0x00030001;
    public static final int MSG_ROLE_LIST_RESPONSE_S2C                  = 0x00030002;
    public static final int MSG_ROLE_CREATE_REQUEST_C2S                 = 0x00030003;
    public static final int MSG_ROLE_CREATE_RESPONSE_S2C                = 0x00030004;
    public static final int MSG_ROLE_DELETE_REQUEST_C2S                 = 0x00030005;
    public static final int MSG_ROLE_DELETE_RESPONSE_S2C                = 0x00030006;
}
