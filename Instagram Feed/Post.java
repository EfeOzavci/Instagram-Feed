
import java.util.ArrayList;

class Post {
    private String postID; // Unique, alphanumeric postID.
    private String content; // Content of the post.
    private User author; // The user that creates the post.
    private ArrayList<User> likes; // Arraylist including users who liked the post.

    public Post(String postID, String content, User author) { // Constructor for Post class.
        this.postID = postID;
        this.content = content;
        this.author = author;
        this.likes = new ArrayList<>();
    }

    public String getPostID() { // Method to return postID.
        return postID;
    }

    public User getAuthor() { // Method to return the user who created the post.
        return author;
    }

    public ArrayList<User> getLikes() { // Method to return users who liked the post.
        return likes;
    }
}