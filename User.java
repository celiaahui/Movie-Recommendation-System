// NOM ET PRENOMS:AHUI ASSY PRUNE-CELIA
// NUMERO ETUDIANT: 300383622

import java.util.ArrayList;
import java.util.List;



public class User {
    private int userID; 
    private List<Integer> likedMovies;
    private List<Integer> dislikedMovies;

    // construis un utilisateur et une liste des films aimés et non aimés
    public User(int userID){
        this.userID=userID;
        this.likedMovies=new ArrayList<>();
        this.dislikedMovies=new ArrayList<>();
    }
    // ajoute les films à liste aimés ou non aimés
    public void addMovie(int movieID, boolean liked){
        if (liked){
            if (!likedMovies.contains(movieID)){
                likedMovies.add(movieID);
                dislikedMovies.remove(Integer.valueOf(movieID));
            }
        }else{
            if(!dislikedMovies.contains(movieID)){
                dislikedMovies.add(movieID);
                likedMovies.remove(Integer.valueOf(movieID));
            }
        }
    }

    // Obtenir l'ID de lutilisateur
    public int getUserID(){
        return userID;
    }

    // Obtenir la liste des films que l'utilisateur n'a pas aimé
    public List<Integer> getDislikedmovies(){
        return new ArrayList<>(dislikedMovies);
    }

    // obtenir la liste des films que l'utilisateur à aimer
    public List<Integer> getLikedmovies(){
        return new ArrayList<>(likedMovies);
    }

    // representation string
    public void printUserInfo(){
        System.out.println("Utilisateur ID: "+ userID);
        System.out.println(" Films aimés :"+ likedMovies);
        System.err.println(" Films non aimés : " + dislikedMovies);

    }
    
}
