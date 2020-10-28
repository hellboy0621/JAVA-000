------

周四作业

总结如下：

使用 GCLogAnalysis.java 演练结果如下图所示：

![1603864955737](C:\Users\hero0\AppData\Roaming\Typora\typora-user-images\1603864955737.png)

- 串行GC
  - Minor GC时，DefNew回收年轻代，以及Tenured回收老年代
  - Full GC时，只Tenured回收老年代
- 并行GC
  - Minor GC时，只PSYoungGen回收年轻代
  - Full GC时，PSYoungGen将年轻代置零，ParOldGen回收老年代
  - 随着内存增大，GC时间增大。
- CMS
  - Minor GC时，只ParNew回收年轻代
  - Full GC时，只CMS回收老年代
  - 随着内存增大，CMS GC时间减小。
  - CMS把GC步骤细化，而且还有与业务并发执行的步骤，所以时间变短。
- G1
  - Evacuation Pause young/mixed 转移暂停 年轻模式/混合模式
  - Full GC



压测性能结果如下图所示：

![1603862839713](C:\Users\hero0\AppData\Roaming\Typora\typora-user-images\1603862839713.png)

结论：

1. 串行GC，随着内存增加，吞吐量反而降低。Minor GC时间和次数明显降低，Major GC时间上升。
2. 并行GC，随着内存增加，吞吐量明显上升。Minor GC时间和次数明显降低，Major GC时间略有上升。
3. CMS，随着内存增加，吞吐量先明显上升后持平。Minor GC时间和次数先降低后持平，Major GC时间先上升后持平。
4. G1，随着内存增加，吞吐量先明显上升后持平。Minor/Major GC时间和次数明显降低。
5. jmc挂载后，会对性能有一定影响。
6. 并行GC吞吐量优于CMS，优于G1。
7. G1与CMS在吞吐量差不多的情况下，G1的gc时间小于CMS。



**CMS处理步骤**

1. Initial Mark 初始标记
2. Concurrent Mark 并发标记
3. Concurrent PreClean 并发预清理
4. Final Remark 最终标记
5. Concurrent Sweep 并发清除
6. Concurrent Reset 并发重置

```wiki
[GC (CMS Initial Mark) [1 CMS-initial-mark: 90819K(174784K)] 101386K(253440K),  0.0005785 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00  secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00  secs]
[CMS-concurrent-abortable-preclean-start]
[CMS-concurrent-abortable-preclean: 0.002/0.071 secs] [Times: user=0.17 sys=0.00,  real=0.07 secs]
[GC (CMS Final Remark) [YG occupancy: 10453 K (78656 K)][Rescan (parallel) ,  0.0004603 secs][weak refs processing, 0.0000042 secs][class unloading, 0.0003021  secs][scrub symbol table, 0.0003918 secs][scrub string table, 0.0001353 secs][1  CMS-remark: 172984K(174784K)] 183437K(253440K), 0.0013362 secs] [Times: user=0.00  sys=0.00, real=0.00 secs]
[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00  secs]
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00  secs]
```



**G1处理步骤**

1. Evacuation Pause(young) 年轻代模式转移暂停
2. Concurrent Marking 并发标记
   - Initial Mark 初始标记
   - Root Region Scan Root区扫描
   - Concurrent Mark 并发标记
   - Remark 再次标记
   - Cleanup 清理
3. Evacuation Pause(mixed) 混合模式转移暂停
4. Full GC

```wiki
[GC pause (G1 Evacuation Pause) (young) 205M->197M(256M), 0.0015229 secs]
[GC pause (G1 Evacuation Pause) (mixed) 210M->195M(256M), 0.0026952 secs]
[GC pause (G1 Humongous Allocation) (young) (initial-mark) 198M->195M(256M),  0.0014825 secs]
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0001070 secs]
[GC concurrent-mark-start]
[GC concurrent-mark-end, 0.0007058 secs]
[GC remark, 0.0008112 secs]
[GC cleanup 203M->203M(256M), 0.0002614 secs]
[GC pause (G1 Evacuation Pause) (young) 211M->202M(256M), 0.0013873 secs]
[GC pause (G1 Evacuation Pause) (mixed)-- 216M->218M(256M), 0.0015985 secs]
[GC pause (G1 Evacuation Pause) (young)-- 224M->224M(256M), 0.0010537 secs]
[Full GC (Allocation Failure)  224M->180M(256M), 0.0222159 secs]
```



------

周六作业：

写一段代码，使用 HttpClient 或 OkHttp 访问 [http://localhost:8801 ](http://localhost:8801/)，代码提交到 Github。

使用HttpClient实现，代码如下

```java

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8801";
        String getResponse = new Client().get(url);
        System.out.println(getResponse);
        String postResponse = new Client().post(url, new HashMap<>());
        System.out.println(postResponse);

    }

    public String get(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(url);
            System.out.println("Executing get request " + httpget.getRequestLine());
            response = httpclient.execute(httpget);
        } finally {
            httpclient.close();
        }
        return handleResponse(response);
    }

    public String post(String url, Map<String, String> body) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response;
        try {
            HttpPost httpPost = new HttpPost(url);
            System.out.println("Executing post request " + httpPost.getRequestLine());
            handleBody(httpPost, body);
            response = httpclient.execute(httpPost);
        } finally {
            httpclient.close();
        }
        return handleResponse(response);
    }

    private void handleBody(HttpPost httpPost, Map<String, String> body) throws UnsupportedEncodingException {
        if (body != null && !body.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = body.entrySet().iterator();
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        }
    }

    private String handleResponse(CloseableHttpResponse response) throws IOException {
        String resultStr = "";
        if (response != null) {
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                if (entity != null) {
                    InputStream inStream = entity.getContent();
                    try {
                        // inStream.read();
                        // do something useful with the response
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inStream.read(buffer)) != -1) {
                            result.write(buffer, 0, length);
                        }
                        resultStr = result.toString(StandardCharsets.UTF_8.name());
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        inStream.close();
                    }
                }
            } finally {
                response.close();
            }
        }
        return resultStr;
    }
}
```



使用OkHttp实现，代码如下：

```java

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class OkHttpClientDemo {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8801";
        String response = new OkHttpClientDemo().run(url);
        System.out.println(response);
        String postResponse = new OkHttpClientDemo().post(url, "");
        System.out.println(postResponse);
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
```







串行GC

Minor GC时，DefNew回收年轻代，以及Tenured回收老年代

Full GC时，只Tenured回收老年代

并行GC

Minor GC时，只PSYoungGen回收年轻代

Full GC时，PSYoungGen将年轻代置零，ParOldGen回收老年代

随着内存增大，GC时间增大。

CMS

Minor GC时，只ParNew回收年轻代

Full GC时，只CMS回收老年代

随着内存增大，CMS GC时间减小。

CMS把GC步骤细化，而且还有与业务并发执行的步骤，所以时间变短。

G1

Evacuation Pause young/mixed 转移暂停 年轻模式/混合模式

Full GC