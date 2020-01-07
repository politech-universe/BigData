import entities.Category;
import entities.Region;
import entities.User;
import entities.Video;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    static String templateId = "https://www.youtube.com/watch?v=%s&gl=%s";
    static String templateUrl = "https://www.youtube.com/feed/trending?gl=";
    private static String key;
    static String analytics = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=%s&key=%s";
    static String channel = "https://www.googleapis.com/youtube/v3/channels?part=statistics&id=%s&key=%s";
    static String snippet = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=%s&key=%s";
    static String category = "https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&id=%s&key=%s";



    static String getDate(String string) {
        //Format: yyyy-mm-dd
        return string.split("T")[0];
    }


    static void getUserInfo(User user) {
        HttpGet request = new HttpGet(String.format(channel, user.getId(), key));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result;
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                result = EntityUtils.toString(entity);
                JSONObject js = new JSONObject(result);

                user.setSubs(Integer.parseInt(js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("statistics")
                        .getString("subscriberCount"))
                );

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void getSnippetInfo(Video cur, User user) {
        HttpGet request = new HttpGet(String.format(snippet, cur.getId(), key));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result;
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                result = EntityUtils.toString(entity);
                JSONObject js = new JSONObject(result);

                cur.setName(js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("snippet")
                        .getString("title")
                );

                cur.setCategory(Integer.parseInt(js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("snippet")
                        .getString("categoryId")))
                ;

                String date = js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("snippet")
                        .getString("publishedAt");

                cur.setDate(getDate(date));

                user.setId(js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("snippet")
                        .getString("channelId"));

                user.setName(js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("snippet")
                        .getString("channelTitle"));

                cur.setUserId(user.getId());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void getStatistics(Video cur) {
        HttpGet request = new HttpGet(String.format(analytics, cur.getId(), key));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result;
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                result = EntityUtils.toString(entity);
                JSONObject js = new JSONObject(result);
                try {
                    cur.setComments(Integer.parseInt(js.getJSONArray("items")
                            .getJSONObject(0)
                            .getJSONObject("statistics")
                            .getString("commentCount")
                    ));
                } catch (org.json.JSONException e) {
                    cur.setComments(0);
                }
                try {
                    cur.setDlikes(Integer.parseInt(js.getJSONArray("items")
                            .getJSONObject(0)
                            .getJSONObject("statistics")
                            .getString("dislikeCount")
                    ));
                } catch (org.json.JSONException e) {
                    cur.setDlikes(0);
                }
                try {
                    cur.setLikes(Integer.parseInt(js.getJSONArray("items")
                            .getJSONObject(0)
                            .getJSONObject("statistics")
                            .getString("likeCount")
                    ));
                } catch (org.json.JSONException e) {
                    cur.setLikes(0);
                }
                try {
                    cur.setViews(Integer.parseInt(js.getJSONArray("items")
                            .getJSONObject(0)
                            .getJSONObject("statistics")
                            .getString("viewCount")
                    ));
                } catch (org.json.JSONException e) {
                    cur.setViews(0);
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void getCategory(Category cat) {
        HttpGet request = new HttpGet(String.format(category, cat.getId(), key));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result;
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                result = EntityUtils.toString(entity);
                JSONObject js = new JSONObject(result);

                cat.setName(js.getJSONArray("items")
                        .getJSONObject(0)
                        .getJSONObject("snippet")
                        .getString("title")
                );


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(cat);
        }
    }

    static void processVideo(String id, int n, int regionId, Connection connection) throws SQLException {
        if (!Connector.hasVideoRegion(id, regionId, connection)) {
            Video curVideo = new Video();
            User curUser = new User();
            Category curCat = new Category();
            curVideo.setId(id);
            curVideo.setRegionId(regionId);
            curVideo.setTrendNum(n + 1);
            curVideo.setTrendDate(LocalDate.now().toString());
            try {
                if (Connector.hasVideo(id, connection)) {
                    Connector.pushVideoRegion(curVideo, connection);
                    System.out.println("NEW REGION VIDEO");
                } else {
                    getStatistics(curVideo);
                    getSnippetInfo(curVideo, curUser);
                    if (!Connector.hasUser(curUser.getId(), connection)) {
                        getUserInfo(curUser);
                        Connector.pushUser(curUser, connection);
                        System.out.println("NEW USER");
                    }
                    curCat.setId(curVideo.getCategory());
                    if (!Connector.hasCategory(curCat.getId(), connection)) {
                        getCategory(curCat);
                        Connector.pushCategory(curCat, connection);
                        System.out.println("NEW CATEGORY");
                    }
                    Connector.pushVideo(curVideo, connection);
                    Connector.pushVideoRegion(curVideo, connection);
                    System.out.println("NEW VIDEO");
                }
            } catch (Exception e) {
                throw new JSONException("Day limit");
            }

        }else{
            System.out.println("ALREADY ADDED");
        }
    }

    static void processWithRegion(Region region, Connection conn) throws IOException, SQLException {
        Document doc = Jsoup.connect(templateUrl + region.getAbb()).maxBodySize(0).get();
        try {
            Element trends = doc.body().selectFirst("ul.expanded-shelf-content-list.has-multiple-items");
            Elements elements = trends.select("div.yt-lockup-thumbnail.contains-addto > a.yt-uix-sessionlink.spf-link");
            List<String> videoId = new ArrayList();
            for (Element element : elements) {
                videoId.add(element.attr("href").split("=")[1]);
            }

            for (int i = 0; i < videoId.size() - 1; i++) {
                processVideo(videoId.get(i), i, region.getId(), conn);
            }
        } catch (Exception e){
            System.out.println("AGAIN BAD REQUEST");
            processWithRegion(region,conn);
        }

    }

    public static void setKey(String key) {
        Parser.key = key;
    }
}
