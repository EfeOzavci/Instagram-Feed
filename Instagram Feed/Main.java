
import java.io.*;

public class Main {
    public static void main(String[] args) {

        // Input and output files.
        String inputFile = args[0];
        String outputFile = args[1];

        // Reader/writer method.
        processInputFile(inputFile, outputFile);
    }

    // Method to read from input file and write to output file.
    public static void processInputFile(String inputFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                String command = parts[0];
                String result;

                // Input file cases.

                switch (command) {

                    case "create_user":
                        result = User.createUser(parts[1]);
                        break;

                    case "follow_user":
                        result = User.followUser(parts[1], parts[2]);
                        break;

                    case "unfollow_user":
                        result = User.unfollowUser(parts[1], parts[2]);
                        break;

                    case "create_post":
                        String userID = parts[1];
                        String postID = parts[2];
                        String[] contentParts = new String[parts.length - 3];
                        System.arraycopy(parts, 3, contentParts, 0, parts.length - 3);
                        String content = String.join(" ", contentParts);
                        result = User.createPost(userID, postID, content);
                        break;
                    case "see_post":
                        result = User.seePost(parts[1], parts[2]);
                        break;

                    case "toggle_like":
                        result = User.toggleLike(parts[1], parts[2]);
                        break;

                    case "see_all_posts_from_user":
                        result = User.seeAllPostsFromUser(parts[1], parts[2]);
                        break;

                    case "generate_feed":
                        int num = Integer.parseInt(parts[2]);
                        result = User.generateFeed(parts[1], num);
                        break;

                    case "scroll_through_feed":
                        String userId = parts[1];
                        int number = Integer.parseInt(parts[2]);
                        String[] likesStrArray = new String[number];
                        System.arraycopy(parts, 3, likesStrArray, 0, number);
                        int[] likes = new int[number];
                        for (int i = 0; i < number; i++) {
                            likes[i] = Integer.parseInt(likesStrArray[i]);
                        }
                        result = User.scrollThroughFeed(userId, number, likes);
                        break;

                    case "sort_posts":
                        result = User.sortPosts(parts[1]);
                        break;

                    default:
                        result = "Error.";
                        break;
                }

                // Writing to output file.

                writer.write(result);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error processing files: " + e.getMessage());
        }
    }
}