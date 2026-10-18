// NOM ET PRENOMS: AHUI ASSY PRUNE-CELIA
// NUMERO ETUDIANT: 300383622



public class Recommendation {
    private double score;
    private int userID;
    private int movieID;
    private int nUsers;
    

    // construis l'Id de l'utilisateur et du film, le scire et le nombre d'utilisateurs
    public Recommendation(int userID, int movieID, double score, int nUsers){
        this.userID=userID;
        this.movieID= movieID;
        this.score= score;
        this.nUsers= nUsers;
    }

    //Obtenir l'Id du film
    public int getMovieID(){
        return movieID;
    }
    
    // Obtenir l'ID de l'utilisateur
    public int getUserID() {
        return userID;

    }
    // Obtenir le nombre d'utilisateur 
    public int getNusers(){
        return nUsers;
    }
    // Obtenir le score
    public double getScore(){
        return score;
    }
    //representation string
    public String toString(){
        return " Utilisateur" +" "+ userID+ " " +"devrait aimer le film" +" "+ movieID+ " "+" avec une probabilité de" +" " +score;
    }
}
