package cn.edu.gdmec.s07150808.rss_test.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.URL;
/**
 * Created by chen on 2017/2/28.
 */
public class HttpUtils {

    private static String URL="http://api.avatardata.cn/ActNews/Query";
    private static String APK_KEY="ca62ca12a2cf4e958317932f3c5e2829";

    public static String setParams(String msg){
        try {
            /*字符串字母格式的编辑*/
            msg = URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL+"?key="+APK_KEY+"&keyword="+msg;
    }
    public static String doGet(String urlStr){
        URL url = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        ByteArrayOutputStream baos = null;
        try {
         /*创建链接 调用openConnection 实现连接*/
            url = new URL(urlStr);

            conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == 200) {
             /*创建输出流*/
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();


            } else {
                throw new Exception("服务器连接错误！ 404");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return   baos.toString();
    }
}
