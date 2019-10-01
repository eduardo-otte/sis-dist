import java.util.HashMap;

public class VoteTally {
    HashMap<String, Integer> tallyCount = new HashMap<String, Integer>();

    /**
     * Adiciona um voto para o valor passado como parâmetro. Cria uma entrada para o valor, se ele possuir
     * nenhum voto.
     * @param key Valor votado
     */
    public void voteFor(String key) {
        if(!tallyCount.containsKey(key)) {
            tallyCount.put(key, 0);
        }
        int currentVotes = tallyCount.get(key);
        tallyCount.put(key, currentVotes + 1);
    }

    /**
     * Busca os votos para o valor passado como parâmetro.
     * @param key Valor para buscar os votos.
     * @return Número de votos para o valor passado
     */
    public int getVotesFor(String key) {
        return tallyCount.get(key);
    }

    /**
     * Retorna a chave que possuir o maior número de votos.
     * @return Chave com o maior número de votos.
     */
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
}
