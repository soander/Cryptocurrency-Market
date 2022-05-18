package com.demo.constant;

/**
 * @ClassName LoginConstant
 * @Description losin constant
 * @Author Yaozheng Wang
 * @Date 2022/5/18 14:40
 **/
public class LoginConstant {

    //administrator
    public static final String ADMIN_TYPE = "admin_type";

    // Membership
    public static final String MEMBER_TYPE  = "member_type";

    // Use username to check mysql
    public static final String QUERY_ADMIN_SQL =
            "SELECT `id` ,`username`, `password`, `status` FROM sys_user WHERE username = ? ";

    // Check user role code
    public static final String QUERY_ROLE_CODE_SQL =
            "SELECT `code` FROM sys_role LEFT JOIN sys_user_role ON sys_role.id = sys_user_role.role_id WHERE sys_user_role.user_id= ?";

    // Admin all permission
    public static final String QUERY_ALL_PERMISSIONS =
            "SELECT `name` FROM sys_privilege";

    // User part permission
    public static final String QUERY_PERMISSION_SQL =
            "SELECT `name` FROM sys_privilege LEFT JOIN sys_role_privilege ON sys_role_privilege.privilege_id = sys_privilege.id LEFT JOIN sys_user_role  ON sys_role_privilege.role_id = sys_user_role.role_id WHERE sys_user_role.user_id = ?";

    public static final String ADMIN_CODE = "ROLE_ADMIN";

    // Member login
    public static final String QUERY_MEMBER_SQL =
            "SELECT `id`,`password`, `status` FROM `user` WHERE mobile = ? or email = ? ";

    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    // Use admin user id to check member name
    public static  final  String QUERY_ADMIN_USER_WITH_ID = "SELECT `username` FROM sys_user where id = ?" ;

    // Use member user id to get member name
    public static  final  String QUERY_MEMBER_USER_WITH_ID = "SELECT `mobile` FROM user where id = ?" ;
}
