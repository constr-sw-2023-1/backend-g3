package br.pucrs.csw.professors.security;

public class RequestContext {

    private static final ThreadLocal<String> accessToken = ThreadLocal.withInitial(String::new);


    public static String getAccessToken() {
        return accessToken.get();
    }

    public static void setAccessToken(String accessToken) {
        RequestContext.accessToken.set(accessToken);
    }
}
