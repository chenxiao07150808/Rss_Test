package cn.edu.gdmec.s07150808.rss_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2017/2/28.
 */
public class GetNewsInfoTake extends AsyncTask<String,Void,List<Map<String,Object>>> {
    /*Activity上下文*/
    private Activity context;
    /*加载提示窗口*/
    private ProgressDialog progressDialog;
    /*错误信息*/
    private String errorMsg="网络错误!";
    /*天气信息列表*/
    private ListView news_info;

    private String new_URL  ;
    /*APL接口的格式*/
    private static String BASE_URL="http://api.avatardata.cn/ActNews/Query?key=";
    private static String key="ca62ca12a2cf4e958317932f3c5e2829&keyword=";

    public GetNewsInfoTake(Activity context){
        this.context=context;
       /*获取提示框*/
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("正在获取新闻，请稍等...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }
    @Override
    protected void onPostExecute(List<Map<String, Object>> maps) {
        super.onPostExecute(maps);
        progressDialog.dismiss();

        if(maps.size()>0){
           news_info= (ListView) context.findViewById(R.id.list_item);
            SimpleAdapter simpleAdapter=new SimpleAdapter(context,maps,R.layout.news_row,new String[]{"title","src","img","pdate_src"},new int[]{R.id.new_title,R.id.name,R.id.icon,R.id.data});
            news_info.setAdapter(simpleAdapter);
            news_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position!=0){
                        Toast.makeText(context,new_URL,Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(new_URL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);

                    }
                }
            });
        }else{
            Toast.makeText(context,errorMsg,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected List<Map<String, Object>> doInBackground(String... params) {
        List <Map<String,Object>> list=new ArrayList<Map<String,Object>>();

        try {
            HttpClient httpClient=new DefaultHttpClient();
            String url=BASE_URL+ key+URLEncoder.encode(params
                    [0],"UTF-8");
            HttpGet httpget=new HttpGet(url);
            HttpResponse response=httpClient.execute(httpget);
            if(response.getStatusLine().getStatusCode()==200){
                String jsonString= EntityUtils.toString(response.getEntity(),"UTF-8");
                JSONObject jsondata=new JSONObject(jsonString);
                if(jsondata.getString("reason").equals("Succes")){
                    JSONArray result=jsondata.getJSONArray("result");
                  /*  JSONArray weatherList=result.getJSONArray("future");*/
                    for(int i=0;i<result.length();i++){

                        Map<String,Object>item=new HashMap<String,Object>();
                        JSONObject resultObject=result.getJSONObject(i);

                         new_URL=resultObject.getString("url");
                        item.put("title",resultObject.getString("title"));
                        item.put("src",resultObject.getString("src"));
                        item.put("img",resultObject.getString("img"));
                        item.put("url",resultObject.getString("url"));
                        item.put("pdate_src",resultObject.getString("pdate_src"));
                        list.add(item);

                    }
                }else{
                    errorMsg="非常抱歉,本应用暂不支持你所请求的城市!";


                }
            }else{
                errorMsg="网络错误，请检查手机是否开启了网络";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    }

