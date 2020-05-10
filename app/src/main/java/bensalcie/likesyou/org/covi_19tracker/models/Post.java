package bensalcie.likesyou.org.covi_19tracker.models;

public class Post {
    String post_id,message,photo,name,person_photo,person_id,time_posted;
    public Post(){

    }

    public Post(String post_id, String message, String photo, String name, String person_photo, String person_id, String time_posted) {
        this.post_id = post_id;
        this.message = message;
        this.photo = photo;
        this.name = name;
        this.person_photo = person_photo;
        this.person_id = person_id;
        this.time_posted = time_posted;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getPerson_photo() {
        return person_photo;
    }

    public String getPerson_id() {
        return person_id;
    }

    public String getTime_posted() {
        return time_posted;
    }
}
