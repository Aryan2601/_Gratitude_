package amhacks.gratitude.Model;

public class Requests {

    String desc, poster, poster_location, type, status, time;

    public Requests() {
    }

    public Requests(String desc, String poster, String poster_location, String type, String status, String time) {
        this.desc = desc;
        this.poster = poster;
        this.poster_location = poster_location;
        this.type = type;
        this.status = status;
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster_location() {
        return poster_location;
    }

    public void setPoster_location(String poster_location) {
        this.poster_location = poster_location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
