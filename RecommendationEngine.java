// Project CSI2120/CSI2520
// Winter 2025
// NOM et PRENOMS: AHUI ASSY PRUNE-CELIA
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

// this is the (incomplete) class that will generate the recommendations for a user
public class RecommendationEngine {
	
	public ArrayList<Movie> movies;
	private Map<Integer, User> users;
	
	// constructs a recommendation engine from files
	public RecommendationEngine(String movieFile, String ratingsFile) throws IOException, 
													NumberFormatException {
		this.movies=new ArrayList<>();
		this.users= new HashMap<>();
		
		movies= new ArrayList<Movie>();
		readMovies(movieFile);
		System.out.println("Appel dde readRatings avec " + ratingsFile);
		readRatings(ratingsFile);
	}
	// Lis le fichier ratings.csv et classe les films aimés et non aimés.
	public void readRatings(String csvFile) throws IOException {
		// Affiche un message indiquant le fichier en cours de lecture
		System.out.println("Lecture du fichier ratings: " + csvFile);
		// Définition du délimiteur utilisé dans le fichier CSV (ici, une virgule ",")
		String delimiter = ",";
		// Ouverture du fichier en mode lecture avec un BufferedReader
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			// Lire la première ligne du fichier (l'en-tête) et l'ignorer
			String line = br.readLine(); // Lire l'en-tête
			
			// Lire chaque ligne du fichier CSV jusqu'à la fin
			while ((line = br.readLine()) != null) {

				// Divise la ligne en plusieurs parties en utilisant la virgule comme séparateur
				String[] parts = line.split(delimiter);
				if (parts.length < 4) continue;
	             
				 // Extraction et conversion des valeurs de la ligne
				try {
					int userID = Integer.parseInt(parts[0].trim());
					int movieID = Integer.parseInt(parts[1].trim());
					double rating = Double.parseDouble(parts[2].trim());
	
					users.putIfAbsent(userID, new User(userID));
	
					if (rating >= 3.5) {
						users.get(userID).addMovie(movieID, true);
					} else if (rating <= 2.5) {
						users.get(userID).addMovie(movieID, false);
						
					}
	
				} catch (NumberFormatException e) {
					System.err.println("Erreur format : " + line);
				}
			}
		}
	}
	
	
	
	// Reads the Movie csv file of the MovieLens dataset
	// It populates the list of Movies
    public void readMovies(String csvFile) throws IOException, 
													NumberFormatException {

        String line;
        String delimiter = ","; // Assuming values are separated by commas

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			// Read each line from the CSV file
			line = br.readLine();
			
			while ((line = br.readLine()) != null && line.length() > 0) {
				// Split the line into parts using the delimiter
				String[] parts = line.split(delimiter);
				String title;
				
				// parse the ID
				int movieID= Integer.parseInt(parts[0]);
				
				if (parts.length < 3)
					throw new NumberFormatException("Error: Invalid line structure: " + line);

				// we assume that the first part is the ID
				// and the last one are genres, the rest is the title
				title= parts[1];
				if (parts.length > 3) {
					
					for (int i=2; i<parts.length-1; i++)
						title+= parts[i];
				}
				
				movies.add(new Movie(movieID,title));
			}
		}
		
			
    }

	public Movie getMovie(int index) {
		return movies.get(index);
	}
	public double computeSimilarity(int userId1, int userId2){
		if (!users.containsKey(userId1) || !users.containsKey(userId2)){
			return 0.0;
		}
		User user1 = users.get(userId1);
		User user2 = users.get(userId2);
	
		Set<Integer> likeU1 = new HashSet<>(user1.getLikedmovies());
		Set<Integer> dislikeU1 = new HashSet<>(user1.getDislikedmovies());
	
		Set<Integer> likeU2 = new HashSet<>(user2.getLikedmovies());
		Set<Integer> dislikeU2 = new HashSet<>(user2.getDislikedmovies());
	
		
		
		Set<Integer> intersectionLikes = new HashSet<>(likeU1);
		intersectionLikes.retainAll(likeU2);
	
		Set<Integer> intersectionDislikes = new HashSet<>(dislikeU1);
		intersectionDislikes.retainAll(dislikeU2);
	
		Set<Integer> unionSet = new HashSet<>(likeU1);
		unionSet.addAll(likeU2);
		unionSet.addAll(dislikeU1);
		unionSet.addAll(dislikeU2);
	
		int unionSize = unionSet.size();
		


		if (unionSize == 0) return 0.0;
	
		return (double) (intersectionLikes.size() + intersectionDislikes.size()) / unionSize;
	}
	
	public List<Movie> recommendMovies(int userId, int N){
		if(!users.containsKey(userId)){
			return new ArrayList<>();
		}
		
		User user=users.get(userId);
		Map<Integer,Double> movieScores= new HashMap<>();

		for(Movie movie: movies){
			int movieId=movie.getID();

			if(user.getLikedmovies().contains(movieId)|| user.getDislikedmovies().contains(movieId)){
				continue;
			}
			int likedUsersCount= 0;
			double score=0.0;
			for (User otherUser: users.values()){
				if(otherUser.getLikedmovies().contains(movieId)){
					likedUsersCount++;
					
					if(otherUser.getUserID()!= userId){
						score+= computeSimilarity(userId, otherUser.getUserID());
					}
				}
			}
			if(likedUsersCount>=10){
				movieScores.put(movieId, score/likedUsersCount);
			}

		}   
		List<Map.Entry<Integer,Double>>sortedMovies= new ArrayList<>(movieScores.entrySet());
		sortedMovies.sort((a,b)-> Double.compare(b.getValue(), a.getValue()));

		List<Movie> recommendations= new ArrayList<>();
		for(int i=0; i< Math.min(N, sortedMovies.size()); i++){
			recommendations.add(getMovieById(sortedMovies.get(i).getKey()));
		}
		// 📌 Affichage amélioré des recommandations avec le score et le nombre d'utilisateurs
        // 📌 Affichage amélioré des recommandations avec le score et le nombre d'utilisateurs
        System.out.println("\n🎬 Recommandations pour User " + userId + ":");
        for (Movie movie : recommendations) {
             int movieId = movie.getID();
              double score = movieScores.getOrDefault(movieId, 0.0); // Récupère le score
             int likedCount = 0;

             // Compter combien d'utilisateurs aiment ce film
            for (User u : users.values()) {
                if (u.getLikedmovies().contains(movieId)) {
                  likedCount++;
                 }
            }

    // Affichage formaté
           System.out.printf("%s - Score : %.4f [%d]\n", movie.getTitle(), score, likedCount);
        }  


		return recommendations;
	}




	private Movie getMovieById(int movieId){
		for(Movie movie: movies){
			if(movie.getID()==movieId){
				return movie;
			}
		}
		return null;
	}

	public Map<Integer, User> getUsers() {
		return users;
	}
	
	
	public int getNumberOfMovies() {
		
		return movies.size();
	}
	
	public static void main(String[] args) {
		if (args.length < 2) { 
			System.err.println("ERREURRRR: Vous devez fournir deux fichiers (movies.csv et ratings.csv)");
			return;
		}
	
		try {
			
			RecommendationEngine rec = new RecommendationEngine(args[0], args[1]);
	
			// Affichage de quelques films pour vérification
			System.out.println("📽️ Liste de quelques films du dataset:");
			for (int i = 0; i < Math.min(20, rec.getNumberOfMovies()); i++) {
				System.out.println(rec.getMovie(i).toString());
			}
	
			// Tester la similarité entre deux utilisateurs
			int user1;
            if (args.length >= 3) {
                try {
                    user1 = Integer.parseInt(args[2]); // Conversion en entier
                } catch (NumberFormatException e) {
                    System.err.println("❌ ERREUR : L'ID utilisateur doit être un nombre entier valide !");
                    return;
                }
            } else {
                // Sélection aléatoire d'un utilisateur si aucun ID n'est fourni
                List<Integer> allUsers = new ArrayList<>(rec.getUsers().keySet());
                if (allUsers.isEmpty()) {
                    System.err.println("❌ ERREUR : Aucun utilisateur trouvé dans les données !");
                    return;
                }
                user1 = allUsers.get(new Random().nextInt(allUsers.size()));
                System.out.println("🔀 Aucun utilisateur spécifié, sélection aléatoire : User " + user1);
            }

            // Vérifier si l'utilisateur existe dans la base
            if (!rec.getUsers().containsKey(user1)) {
                System.out.println("❌ L'utilisateur " + user1 + " n'existe pas dans les données !");
                return;
            }

            // Sélection d'un autre utilisateur pour comparaison (différent de user1)
            List<Integer> otherUsers = new ArrayList<>(rec.getUsers().keySet());
            otherUsers.remove(Integer.valueOf(user1));
            int user2 = otherUsers.get(new Random().nextInt(otherUsers.size())); // Choisir un utilisateur au hasard

            // Calcul de la similarité entre les utilisateurs
            double similarity = rec.computeSimilarity(user1, user2);
            System.out.println("🔍 Similarité entre User " + user1 + " et User " + user2 + " : " + similarity);
	
			// Tester les recommandations
			int N = 5; // Nombre de films recommandés
			List<Movie> recommendations = rec.recommendMovies(user1, N);
	
			System.out.println("\n🎬 Recommandations pour User " + user1 + ":");
			for (Movie movie : recommendations) {
				System.out.println(movie);
			}
	
		} catch (Exception e) {
			System.err.println("❌ Erreur lors de l'exécution: " + e.getMessage());
		}
	}
	

}
