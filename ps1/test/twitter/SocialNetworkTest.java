package twitter;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class SocialNetworkTest {

    /*
     * Testing strategy:
     * 
     * For guessFollowsGraph():
     *  - empty list of tweets
     *  - tweets with no mentions
     *  - tweet with one mention
     *  - tweet with multiple mentions
     *  - multiple tweets from same user
     * 
     * For influencers():
     *  - empty graph
     *  - single user without followers
     *  - single influencer
     *  - multiple influencers
     *  - tied influence
     */

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // ensure assertions are enabled
    }

    // 1. Empty List of Tweets
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // 2. Tweets Without Mentions
    @Test
    public void testTweetsWithoutMentions() {
        Tweet t1 = new Tweet(1, "alice", "Hello everyone", new Date());
        Tweet t2 = new Tweet(2, "bob", "Good morning", new Date());
        List<Tweet> tweets = Arrays.asList(t1, t2);

        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph when no mentions", graph.isEmpty());
    }

    // 3. Single Mention
    @Test
    public void testSingleMention() {
        Tweet t1 = new Tweet(1, "alice", "hi @bob", new Date());
        List<Tweet> tweets = Arrays.asList(t1);

        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(graph.containsKey("alice"));
        assertTrue(graph.get("alice").contains("bob"));
    }

    // 4. Multiple Mentions
    @Test
    public void testMultipleMentions() {
        Tweet t1 = new Tweet(1, "alice", "@bob and @charlie let's meet!", new Date());
        List<Tweet> tweets = Arrays.asList(t1);

        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(Set.of("bob", "charlie"), graph.get("alice"));
    }

    // 5. Multiple Tweets from One User
    @Test
    public void testMultipleTweetsFromSameUser() {
        Tweet t1 = new Tweet(1, "alice", "hi @bob", new Date());
        Tweet t2 = new Tweet(2, "alice", "@charlie how are you?", new Date());
        List<Tweet> tweets = Arrays.asList(t1, t2);

        Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
        assertEquals(Set.of("bob", "charlie"), graph.get("alice"));
    }

    // 6. Empty Graph for influencers()
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }

    // 7. Single User Without Followers
    @Test
    public void testSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = Map.of("alice", Set.of());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected single user", List.of("alice"), influencers);
    }

    // 8. Single Influencer
    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", Set.of("bob"));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected bob first", "bob", influencers.get(0));
    }

    // 9. Multiple Influencers
    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", Set.of("bob", "charlie"));
        followsGraph.put("dave", Set.of("charlie"));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("charlie should be first", "charlie", influencers.get(0));
    }

    // 10. Tied Influence
    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", Set.of("bob"));
        followsGraph.put("dave", Set.of("charlie"));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected both bob and charlie", influencers.containsAll(List.of("bob", "charlie")));
    }
}

