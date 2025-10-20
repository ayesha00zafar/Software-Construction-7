package twitter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();

        Pattern mentionPattern = Pattern.compile("@(\\w+)");

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
            Matcher matcher = mentionPattern.matcher(tweet.getText());

            Set<String> mentions = followsGraph.getOrDefault(author, new HashSet<>());
            while (matcher.find()) {
                String mentionedUser = matcher.group(1).toLowerCase();
                if (!mentionedUser.equals(author)) { // avoid self-follow
                    mentions.add(mentionedUser);
                }
            }
            if (!mentions.isEmpty()) {
                followsGraph.put(author, mentions);
            }
        }

        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();

        // Initialize all users
        for (String user : followsGraph.keySet()) {
            followerCount.putIfAbsent(user, 0);
            for (String followed : followsGraph.get(user)) {
                followerCount.put(followed, followerCount.getOrDefault(followed, 0) + 1);
            }
        }

        List<String> users = new ArrayList<>(followerCount.keySet());
        users.sort((a, b) -> followerCount.get(b) - followerCount.get(a));

        return users;
    }
}

