package com.giveangel.sender.utils;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by Kyungman on 2015-05-07.
 */

abstract public class Controller {
    // URL
    public static final String COLON = ":";
    public static final String URL_SEPARATOR = "/";

    public static final String CONTEXT = "hw" + URL_SEPARATOR + "photomms"
            + URL_SEPARATOR;
    public static final String DOMAIN = "http://85114.com";
    public static final String URL_ROOT = DOMAIN + URL_SEPARATOR
            + CONTEXT;
    /* result) http://DOMAIN/hw/photomms/ */

    private Context context;

    public Controller(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    // getInputStreamFromUrl : 주어진 URL 에 대한 입력 스트림(InputStream)을 얻는다.
    public static InputStream getInputStreamFromUrl(String url) {
        InputStream contentStream = null;
        try {
            // HttpClient 를 사용해서 주어진 URL에 대한 입력 스트림을 얻는다.
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            contentStream = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentStream;
    } // getInputStreamFromUrl

    public static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) {
        T data = null;
        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception e) {
            // Handle the problem
        }
        return data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrl(String context, HashMap<Object, Object> params) {
        return URLManager.getUrl(context, params);
    }

    // getStringFromUrl : 주어진 URL 페이지를 문자열로 얻는다.
    public String getStringFromUrl(String url)
            throws UnsupportedEncodingException {
        // 입력스트림을 "UTF-8" 를 사용해서 읽은 후, 라인 단위로 데이터를 읽을 수 있는 BufferedReader 를
        // 생성한다.
        BufferedReader br = new BufferedReader(new InputStreamReader(
                getInputStreamFromUrl(url), "UTF-8"));
        // 읽은 데이터를 저장한 StringBuffer 를 생성한다.
        StringBuffer sb = new StringBuffer();
        try {
            // 라인 단위로 읽은 데이터를 임시 저장한 문자열 변수 line
            String line = null;
            // 라인 단위로 데이터를 읽어서 StringBuffer 에 저장한다.
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    } // getStringFromUrl

}
