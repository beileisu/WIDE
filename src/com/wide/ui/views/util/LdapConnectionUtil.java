package com.wide.ui.views.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapConnectionUtil {

    private String ldapURL;
    private Integer ldapPort = 389;
    private String baseDN;
    private String ldapUserName;
    private String ldapPassword;

    public LdapConnectionUtil(String ldapURL, Integer ldapPort, String baseDN, String ldapUserName, String ldapPassword) {
        this.ldapURL = ldapURL;
        this.ldapPort = ldapPort;
        this.baseDN = baseDN;
        this.ldapUserName = ldapUserName;
        this.ldapPassword = ldapPassword;
    }

    public LdapConnectionUtil(String ldapURL, String baseDN, String ldapUserName, String ldapPassword) {
        this.ldapURL = ldapURL;
        this.baseDN = baseDN;
        this.ldapUserName = ldapUserName;
        this.ldapPassword = ldapPassword;
    }
    
    public String getLdapURL() {
        return ldapURL;
    }

    public void setLdapURL(String ldapURL) {
        this.ldapURL = ldapURL;
    }

    public Integer getLdapPort() {
        return ldapPort;
    }

    public void setLdapPort(Integer ldapPort) {
        this.ldapPort = ldapPort;
    }

    public String getBaseDN() {
        return baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    public String getLdapUserName() {
        return ldapUserName;
    }

    public void setLdapUserName(String ldapUserName) {
        this.ldapUserName = ldapUserName;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }

    public boolean authenticate() {
        boolean bRtn = false;// 标注是否验证成功，初始为false

        Hashtable<String, String> env = new Hashtable<String, String>(4);
        // LDAP 服务器的 URL 地址，
        // env 中的key都是固定值在 javax.naming.Context 类中
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");// ldapCF
        env.put(Context.PROVIDER_URL, getLdapURL());// ldapURL
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); // ldapAuthMode
        // username和对应的password怎么在LDAP服务器中设置，我也不知道
        // 通过默认的用户名"cn=manager,dc=aaa,dc=bbb"(aaa、bbb的具体值要在配置文件中配置，具体看参考博文)和密码"secret",可以测试连接是否成功
        env.put(Context.SECURITY_PRINCIPAL, getLdapUserName());
        env.put(Context.SECURITY_CREDENTIALS, getLdapPassword());
        DirContext ctx = null;
        try {
            // 这条代码执行成功就是验证通过了，至于为什么我也不知道
            ctx = new InitialDirContext(env);
            bRtn = true;
            System.out.println("Ldap验证通过!");
        } catch (Exception ex) {
            System.out.println("Ldap 初始化 出错:" + ex);
        } finally {
            try {
                if (ctx != null) {
                    ctx.close();
                    ctx = null;
                }
                env.clear();
            } catch (Exception e) {
                System.out.println("Ldap context close出错:" + e);
            }
        }

        // 验证成功返回 true，验证失败返回false
        return bRtn;
    }

    public void findUser(String umAccount) throws NamingException {
        LdapContext ctx = connetLDAP();
        String userinfo = "";
        int flag = 0;
        // 设置搜索过滤条件
        String name = "ou=people,cn=administrativeldap,cn=windchill_11.0,o=cccar";
        // 定制返回属性
        SearchControls searchControls = new SearchControls();
        // 设置搜索范围
        NamingEnumeration<SearchResult> answer = ctx.search(name, "(uid=wcadmin)", searchControls);
        System.out.println();
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.nextElement();
            Attributes attrs = sr.getAttributes();
            Attribute attribute = attrs.get("userPassword");

            String value = new String((byte[]) attribute.get());

            System.out.println(value);
        }

        System.out.println(userinfo);
    }

    public LdapContext connetLDAP() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, getLdapURL());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getLdapUserName());
        env.put(Context.SECURITY_CREDENTIALS, getLdapPassword());
        LdapContext ctxTDS = new InitialLdapContext(env, null);
        System.out.println(ctxTDS);
        return ctxTDS;
    }

    public static void main(String[] args) throws NamingException {
        String ldapURL = "LDAP://192.168.197.132:389/";
        String baseDN = "o=ptc";
        String ldapUserName = "cn=Manager";
        String ldapPassword = "wcadmin";
        LdapConnectionUtil ldapConnectionUtil = new LdapConnectionUtil(ldapURL, baseDN, ldapUserName, ldapPassword);
        boolean isAuth = ldapConnectionUtil.authenticate();
        System.out.println("LdapConnection : " + isAuth);

    }

}
