package cn.farmFish.service.webserviceApi.bean;

/**
 * Created by Administrator on 2016/12/5.
 */
public  class NewsInfo{
       private String articleid;
       private String title;
       private String createddate;
       private String url;

        public String getArticleid() {
            return articleid;
        }

        public void setArticleid(String articleid) {
            this.articleid = articleid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreateddate() {
            return createddate;
        }

        public void setCreateddate(String createddate) {
            this.createddate = createddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "NewsInfo{" +
                    "articleid='" + articleid + '\'' +
                    ", title='" + title + '\'' +
                    ", createddate='" + createddate + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

/**

 {"GetNewsListResult":"[{\"articleid\":\"3070cb88-b14a-48db-a7cd-f2c4faa1476e\",\"title\":\"鱼塘大量鱼虾死亡，养殖户疑饲料所致\",\"createddate\":\"2016-10-18T16:51:38\",\"url\":\"http:\/\/183.78.182.98:9005\/PagesBaseInfo\/NewsViewMobile.aspx?ID=3070cb88-b14a-48db-a7cd-f2c4faa1476e\"}]"}

 */