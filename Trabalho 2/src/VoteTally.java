import java.util.HashMap;

public class VoteTally {
    HashMap<String, Integer> tallyCount = new HashMap<String, Integer>();

    public void voteFor(String key) {
        if(!tallyCount.containsKey(key)) {
            tallyCount.put(key, 0);
        }
        int currentVotes = tallyCount.get(key);
        tallyCount.put(key, currentVotes + 1);
    }

    public int getVotesFor(String key) {
        return tallyCount.get(key);
    }

    public String getMostVoted() {
        String mostVoted = "";
        for(String key : tallyCount.keySet()) {
            if(mostVoted.isEmpty()) {
                mostVoted = key;
            } else {
                if(getVotesFor(key) > getVotesFor(mostVoted))
                    mostVoted = key;
            }
        }

        return mostVoted;
    }

    public int getTotalVotes() {
        int totalVotes = 0;
        for(String key : tallyCount.keySet())
            totalVotes += tallyCount.get(key);

        return totalVotes;
    }
}
