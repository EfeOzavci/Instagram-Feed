
import java.util.ArrayList;

public class User {

    private static HashMap<String, User> users = new HashMap<>(); // Hashmap storing <userID, User> pairs.
    private static HashMap<String, Post> posts = new HashMap<>(); // Hashmap storing <postID, Post> pairs.

    private String userID; // Unique, alphanumeric user ID.

    private ArrayList<User> following; // Arraylist storing the users that User follows.
    private ArrayList<User> followers; // Arraylist storing the users that follows User.
    private ArrayList<Post> userPosts; // Arraylist storing the Posts created by User.
    private ArrayList<Post> seenPosts; // Arraylist storing the Posts that User saw.

    private User(String userID) { // Constructor for User class.
        this.userID = userID;
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.userPosts = new ArrayList<>();
        this.seenPosts = new ArrayList<>();
    }

    public String getUserID() { // Method to return userID.
        return userID;
    }

    public ArrayList<Post> getSeenPosts() { // Method to return the Posts that is seen.
        return seenPosts;
    }

    public void markPostAsSeen(Post post) { // Method to label the posts as seen.
        if (post != null) {
            seenPosts.add(post);
        }
    }

    public static String createUser(String userID) { // Method to create a user with given userID.
        if (users.containsKey(userID)) {
            return "Some error occurred in create_user."; // If such an userID exists, return error.
        }
        User newUser = new User(userID); // Create the new user.
        users.put(userID, newUser); // Put the <userID, User> pair in the users Hashmap.
        return "Created user with Id " + userID + ".";
    }

    public static String followUser(String userID1, String userID2) { // Method to make the User with ID userID1 follow the User with ID userID2.
        User user1 = users.get(userID1); // Get user1 from Hashmap.
        User user2 = users.get(userID2); // Get user2 from Hashmap.

        // Return error if any of the users doesn't exist or if their IDs are equal or if user1 is already following user2.
        if (user1 == null || user2 == null || userID1.equals(userID2) || user1.following.contains(user2)) {
            return "Some error occurred in follow_user.";
        }

        user1.following.add(user2); // Add user2 to user1's following arraylist.
        user2.followers.add(user1); // Add user1 to user2's followers arraylist.

        return userID1 + " followed " + userID2 + ".";
    }

    public static String unfollowUser(String userID1, String userID2) { // Method to make the User with ID userID1 unfollow the User with ID userID2.
        User user1 = users.get(userID1); // Get user1 from Hashmap.
        User user2 = users.get(userID2); // Get user2 from Hashmap.

        // Return error if any of the users doesn't exist or if their IDs are equal or if user1 is not following user2.
        if (user1 == null || user2 == null || userID1.equals(userID2) || !user1.following.contains(user2)) {
            return "Some error occurred in unfollow_user.";
        }

        user1.following.remove(user2); // Remove user2 from user1's following arraylist.
        user2.followers.remove(user1); // Remove user1 from user2's followers arraylist.

        return userID1 + " unfollowed " + userID2 + ".";
    }

    public static String createPost(String userID, String postID, String content) { // Method to create a post with given userID, postID, and content.
        User user = users.get(userID); // Get user from Hashmap.

        // Return error if such user doesn't exist or a Post with ID postID already exists.
        if (user == null || posts.containsKey(postID)) {
            return "Some error occurred in create_post.";
        }

        Post newPost = new Post(postID, content, user); // Create the new post.
        posts.put(postID, newPost); // Put the <postID, Post> pair to posts Hashmap.
        user.userPosts.add(newPost); // Add the post to user's userPosts arraylist.

        return userID + " created a post with Id " + postID + ".";
    }

    public static String seePost(String userID, String postID) { // Method to see a post with given userID and postID.
        User user = users.get(userID); // Get user from Hashmap.
        Post post = posts.get(postID); // Get post from Hashmap.

        // Return error if such a user or post doesn't exist.
        if (user == null || post == null) {
            return "Some error occurred in see_post.";
        }

        user.markPostAsSeen(post); // Label the post as seen.

        return userID + " saw " + postID + ".";
    }

    public static String seeAllPostsFromUser(String viewerID, String viewedID) { // Method to see all posts of a user with given viewerID and viewedID.
        User viewer = users.get(viewerID); // Get viewer user from Hashmap.
        User viewed = users.get(viewedID); // Get viewed user from Hashmap.

        // Return error if any of the users doesn't exist.
        if (viewer == null || viewed == null) {
            return "Some error occurred in see_all_posts_from_user.";
        }

        // Create an arraylist to store all the posts of viewed user.
        ArrayList<Post> viewedPosts = viewed.userPosts;

        // Label all posts as seen by the viewer user.
        for (Post post : viewedPosts) {
            viewer.markPostAsSeen(post);
        }

        return viewerID + " saw all posts of " + viewedID + ".";
    }

    public static String toggleLike(String userID, String postID) {
        User user = users.get(userID); // Get the user from Hashmap.
        Post post = posts.get(postID); // Get the post from Hashmap.

        // Return error if any of the user or post doesn't exist.
        if (user == null || post == null) {
            return "Some error occurred in toggle_like.";
        }
        // Toggle the like.
        if (post.getLikes().contains(user)) { // If the post is already liked, unlike the post.
            post.getLikes().remove(user); // Unlike.
            return userID + " unliked " + postID + ".";
        } else {
            post.getLikes().add(user); // Like
            user.markPostAsSeen(post); // Label the post as seen by user.
            return userID + " liked " + postID + ".";
        }
    }

    public static String generateFeed(String userId, int num) { // Method to generate feed with given userId and num.
        User user = users.get(userId); // Get the user from Hashmap.
        if (user == null) { // Return error if such a user doesn't exist.
            return "Some error occurred in generate_feed.";
        }

        ArrayList<Post> feed = new ArrayList<>(); // Create the feed arraylist.

        for (User u : user.following) { // For each user that User is following;
            for (Post p : u.userPosts) { // For each post that user created;
                if (user.seenPosts.contains(p)) { // If the User have seen it before, continue.
                    continue;
                }
                feed.add(p); // Else, add it to feed.
            }
        }

        feed = mergeSortPosts(feed); // Sort the feed by descending order using mergeSortPosts method.

        StringBuilder feedLog = new StringBuilder("Feed for " + userId + ":\n"); // Start logging.

        int count = 0;

        for (Post post : feed) { // For each post in the feed;
            if (count == num) { // If the num equals to the number of 1s and 0s, break.
                break;
            }
            // Else keep logging.
            feedLog.append("Post ID: ").append(post.getPostID())
                    .append(", Author: ").append(post.getAuthor().getUserID())
                    .append(", Likes: ").append(post.getLikes().size())
                    .append("\n");
            count++; // Increment the count.
        }

        if (count < num) { // Log the last statement.
            feedLog.append("No more posts available for ").append(userId).append(".");
        }
        return feedLog.toString().trim();
    }

    public static String scrollThroughFeed(String userId, int num, int[] likes) { // Method to scroll through feed with given userID, num, and likes.
        User user = users.get(userId); // Get the user from Hashmap.

        // Return error if any of the user of likes are null.
        if (user == null || likes == null) {
            return "Some error occurred in scroll_through_feed.";
        }

        StringBuilder log = new StringBuilder(userId + " is scrolling through feed:"); // Start logging.

        ArrayList<Post> feed = generateFeedList(user); // Generate the feed arraylist.

        int count = 0;
        int i = 0;
        while (i < feed.size() && count < num) { // While the feed size is not reached and num is not reached;
            Post post = feed.get(i); // Get post from the feed.

            if (user.getSeenPosts().contains(post)) { // Skip the posts that are already seen by user.
                i++; // Increment index.
                continue;
            }

            user.markPostAsSeen(post); // Label the post as seen by user.

            if (likes[count] == 1) { // If 1, like the post.

                post.getLikes().add(user); // Like.
                log.append("\n").append(userId).append(" saw ").append(post.getPostID())
                        .append(" while scrolling and clicked the like button.");

            } else { // Else do not like the post.
                log.append("\n").append(userId).append(" saw ").append(post.getPostID())
                        .append(" while scrolling.");
            }
            count++; // Increment count.
            i++; // Increment index.
        }

        if (count < num) { // Finish logging.
            log.append("\nNo more posts in feed.");
        }
        return log.toString().trim();
    }

    private static ArrayList<Post> generateFeedList(User user) { // Method to generate feed for scrollThroughFeed method.

        ArrayList<Post> feed = new ArrayList<>(); // Create feed arraylist.

        for (User u : user.following) { // For each user that User is following;
            for (Post p : u.userPosts) { // For each post that user created;
                if (user.seenPosts.contains(p)) { // If the User have seen it before, continue.
                    continue;
                }
                feed.add(p); // Else, add it to feed.
            }
        }

        feed = mergeSortPosts(feed); // Sort the feed by descending order using mergeSortPosts method.

        return feed;
    }

    public static String sortPosts(String userId) { // Method to sort posts of a user with given userId.
        User user = users.get(userId); // Get the user from Hashmap.

        // Return error if the user doesn't exist or the user didn't create any post.
        if (user == null || user.userPosts == null) {
            return "Some error occurred in sort_posts.";
        }

        StringBuilder log = new StringBuilder("Sorting " + userId + "'s posts:"); // Start logging.

        ArrayList<Post> postsList = new ArrayList<>(user.userPosts); // Create an arraylist including user's posts.

        postsList = mergeSortPosts(postsList); // Sort the posts using merge sort.

        for (Post post : postsList) { // For each post in postsList;
            log.append("\n").append(post.getPostID()).append(", Likes: ").append(post.getLikes().size()); // Log the posts.
        }
        return log.toString();
    }

    private static ArrayList<Post> mergeSortPosts(ArrayList<Post> postsList) { // Method to merge sort the given arraylist.

        if (postsList.size() <= 1) { // Return the list if it has 0 or 1 elements, it's already sorted.
            return postsList;
        }

        int mid = postsList.size() / 2; // Find the middle index to split the list into two.

        ArrayList<Post> left = new ArrayList<>(postsList.subList(0, mid)); // Create a new list for the left half of the original list. Copy from the original list.

        ArrayList<Post> right = new ArrayList<>(postsList.subList(mid, postsList.size())); // Create a new list for the right half of the original list. Copy from the original list.

        left = mergeSortPosts(left); // Sort the left half recursively.

        right = mergeSortPosts(right); // Sort the right half recursively.

        return merge(left, right); // Merge the two sorted lists into a single one and return.
    }

    private static ArrayList<Post> merge(ArrayList<Post> left, ArrayList<Post> right) { // Method to merge two sorted lists into one sorted list

        ArrayList<Post> merged = new ArrayList<>(); // Create an arraylist to hold the merged sorted elements.

        // Initialize pointers for the left and right lists.
        int i = 0; // Pointer for the left list.
        int j = 0; // Pointer for the right list.


        while (i < left.size() && j < right.size()) { // Merge elements from left and right lists in sorted order.

            if (comparePosts(left.get(i), right.get(j)) <= 0) { // If left element is less than or equal to right element;
                merged.add(left.get(i++)); // add the left element to the merged list and increment i.

            } else { //Else;
                merged.add(right.get(j++)); // add the right element to the merged list and increment j.
            }
        }

        while (i < left.size()) { // After the main loop, add any remaining elements from the left list.
            merged.add(left.get(i++));
        }

        while (j < right.size()) { // Add any remaining elements from the right list.
            merged.add(right.get(j++));
        }

        return merged; // Return the merged list, which is now sorted.
    }

    private static int comparePosts(Post p1, Post p2) { // Method for comparing two posts.
        int likes1 = p1.getLikes().size(); // Number of likes for post p1.
        int likes2 = p2.getLikes().size(); // Number of likes for post p2.

        // Compare the number of likes in descending order.
        if (likes2 > likes1) { // If p2 has more likes than p1, p2 should come before p1.
            return 1;

        } else if (likes2 < likes1) { // If p1 has more likes than p2, p1 should come before p2.
            return -1;

        } else { //If both posts have the same number of likes,compare their post IDs in descending lexicographical order

            // Returns a positive number if p2's ID is lexicographically greater than p1's ID . Zero if they are equal. A negative number if p2's ID is lexicographically less than p1's ID.
            return p2.getPostID().compareTo(p1.getPostID());
        }
    }
}