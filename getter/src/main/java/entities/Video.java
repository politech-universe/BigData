package entities;

public class Video {
    private String id;
    private String name;
    private String userId;
    private int views;
    private int regionId;
    private int category;
    private int trendNum;
    private int dlikes;
    private int likes;
    private String date;
    private int comments;
    private String trendDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTrendNum() {
        return trendNum;
    }

    public void setTrendNum(int trendNum) {
        this.trendNum = trendNum;
    }

    public int getDlikes() {
        return dlikes;
    }

    public void setDlikes(int dlikes) {
        this.dlikes = dlikes;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Video: " + "id:" + id + " name:" + name +
                " userId:" + userId +
                " views:" + views +
                " category:" + category +
                " trend:" + trendNum +
                " dislikes:" + dlikes +
                " likes:" + likes +
                " comments:" + comments +
                " date:" + date +
                " trend_date:" + trendDate +
                " region:" + regionId;
    }

    public String getTrendDate() {
        return trendDate;
    }

    public void setTrendDate(String trendDate) {
        this.trendDate = trendDate;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }
}
