
# 🎬 Movie Recommendation System

A Java-based movie recommendation system that uses **user-based collaborative filtering** to generate personalized movie recommendations based on shared user preferences.

Developed as part of the **CSI2120/CSI2520 academic project (Winter 2025)**, this application explores how similarity measures, data structures, and recommendation algorithms can transform movie ratings into personalized suggestions.

The system processes movie and rating datasets, identifies similarities between users, calculates recommendation scores, and ranks movies based on their relevance to a selected user.

## 🚀 Key Features

### 1. Movie and Rating Data Processing
- Loads movie information from `movies.csv`.
- Reads user ratings from `ratings.csv`.
- Creates and manages user profiles.
- Categorizes movie ratings into positive and negative preferences.
- Uses Java collections to organize movie and user data.

### 2. User Preference Classification

The application converts numerical ratings into two categories:

| Rating | Classification |
|--------|----------------|
| 3.5 or higher | Liked |
| 2.5 or lower | Disliked |
| Between 2.5 and 3.5 | Not classified |

Each user maintains separate lists of liked and disliked movies.

The system also prevents duplicate movie entries and ensures that a movie cannot simultaneously appear in both preference lists.

### 3. User Similarity Calculation

The recommendation engine calculates similarity between users based on movies they both liked or disliked.

It uses Java `HashSet` operations to identify intersections and unions between user preferences.

The similarity formula is:

```text
Similarity(A, B) =
    (Common Liked Movies + Common Disliked Movies)
    ------------------------------------------------
           Total Unique Rated Movies
```

More formally:

```text
S(A,B) = (|LA ∩ LB| + |DA ∩ DB|)
         -----------------------
         |LA ∪ LB ∪ DA ∪ DB|
```

Where:

- `LA`: Movies liked by user A
- `LB`: Movies liked by user B
- `DA`: Movies disliked by user A
- `DB`: Movies disliked by user B

The similarity score ranges from 0 to 1.

If either user does not exist or the union is empty, the method returns `0.0`.

### 4. Personalized Movie Recommendations

For a selected user, the recommendation engine:

1. Examines movies in the dataset.
2. Excludes movies the user has already liked or disliked.
3. Identifies other users who liked each candidate movie.
4. Calculates similarity between the selected user and those users.
5. Adds the similarity scores together.
6. Divides the total by the number of users who liked the movie.
7. Retains movies liked by at least 10 users.
8. Sorts the eligible movies by recommendation score.
9. Returns the highest-ranked recommendations.

This approach combines user similarity with a minimum popularity threshold to generate personalized suggestions.

### 5. Flexible User Selection

The application supports two ways to select a user:

- **Manual selection:** Provide a user ID as a command-line argument.
- **Random selection:** If no user ID is provided, the application randomly selects an existing user.

The application also selects another user to demonstrate its similarity calculation.

### 6. Console-Based Output

The application displays:

- A sample of movies loaded from the dataset.
- The selected user's ID.
- Similarity between two users.
- Recommended movie titles.
- Recommendation scores.
- Number of users who liked each recommended movie.

## 🛠️ Technologies and Concepts

| Technology / Concept | Application |
|----------------------|-------------|
| Java | Core implementation |
| Object-Oriented Programming | Class-based architecture and encapsulation |
| ArrayList | Storing movies, preferences, and results |
| HashMap | Mapping user IDs to user objects and movie IDs to scores |
| HashSet | Computing intersections and unions of preferences |
| Comparator / Lambda | Sorting recommendations by descending score |
| BufferedReader / FileReader | Reading CSV datasets |
| Collaborative Filtering | Generating recommendations using user similarity |
| Git & GitHub | Source code hosting and version control |

## 📂 Project Structure

```text
Movie-Recommendation-System/
├── Movie.java
├── User.java
├── Recommendation.java
├── RecommendationEngine.java
├── movies.csv
├── ratings.csv
├── .gitignore
└── README.md
```

### Class Responsibilities

#### Movie.java

Represents a movie in the dataset.

**Attributes:**
- `movieID`: Unique movie identifier.
- `title`: Movie title.

**Methods:**
- `getID()`
- `getTitle()`
- `toString()`

#### User.java

Represents a user and stores their movie preferences.

**Attributes:**
- `userID`
- `likedMovies`
- `dislikedMovies`

**Methods:**
- `addMovie(int movieID, boolean liked)`
- `getUserID()`
- `getLikedmovies()`
- `getDislikedmovies()`
- `printUserInfo()`

The preference getters return copies of the internal lists to protect the stored data from external modification.

#### Recommendation.java

Defines a data model for an individual recommendation.

**Attributes:**
- `userID`
- `movieID`
- `score`
- `nUsers`

It provides getter methods and a string representation.

**Implementation note:** The current recommendation engine returns `List<Movie>` objects rather than using this `Recommendation` class directly.

#### RecommendationEngine.java

Contains the application's primary recommendation logic.

**Main responsibilities:**
- Load movie data.
- Parse user ratings.
- Build user profiles.
- Compute user similarity.
- Calculate recommendation scores.
- Filter and rank candidate movies.
- Display results.
- Handle command-line arguments.

**Key methods:**
- `readMovies(String csvFile)`
- `readRatings(String csvFile)`
- `computeSimilarity(int userId1, int userId2)`
- `recommendMovies(int userId, int N)`
- `getMovieById(int movieId)`
- `getUsers()`
- `getNumberOfMovies()`
- `main(String[] args)`

## 🧮 Recommendation Algorithm

### Step 1 — Load the datasets

The `RecommendationEngine` constructor initializes an `ArrayList<Movie>` and a `HashMap<Integer, User>`.

It then calls `readMovies()` and `readRatings()` to populate these collections.

### Step 2 — Build user profiles

For each rating, the engine identifies the user and movie, then classifies the rating using the configured thresholds.

Users are created automatically when their IDs are encountered for the first time.

### Step 3 — Calculate similarity

The engine converts users' preference lists into sets and calculates:

- Intersection of liked movies.
- Intersection of disliked movies.
- Union of all liked and disliked movies.

The resulting similarity score measures agreement relative to the total number of unique movies appearing in the two users' preference lists.

### Step 4 — Score candidate movies

For each unseen movie, the engine sums the similarities between the target user and other users who liked that movie.

The score is calculated as:

```text
Recommendation Score(movie, user) =

    Sum of similarity(user, otherUser)
    for other users who liked the movie
    ------------------------------------
    Number of users who liked the movie
```

A movie is considered eligible only if at least **10 users** liked it.

### Step 5 — Rank recommendations

Eligible movies are stored in a `HashMap<Integer, Double>` containing movie IDs and their recommendation scores.

The map entries are converted into a list and sorted in descending score order.

The method returns up to `N` movies, depending on the number of eligible candidates.

The main program requests **5 recommendations** by default.

## 💻 How to Run

### Requirements

- Java Development Kit (JDK), such as Java 17
- Eclipse IDE or another Java-compatible development environment
- `movies.csv` and `ratings.csv`

### Option 1 — Eclipse

1. Clone or download the repository.
2. Open Eclipse.
3. Create a new Java project named `MovieRecommendationSystem`.
4. Import the four `.java` files into the `src` folder.
5. Place `movies.csv` and `ratings.csv` in the project root.
6. Open `Run → Run Configurations`.
7. Select `Java Application → RecommendationEngine`.
8. Open the `Arguments` tab.
9. Enter:

```text
movies.csv ratings.csv
```

10. Set the working directory to the project root.
11. Click **Apply**, then **Run**.

### Option 2 — Command Line

Clone the repository:

```bash
git clone https://github.com/celiaahui/Movie-Recommendation-System.git
cd Movie-Recommendation-System
```

Compile the Java classes:

```bash
javac Movie.java User.java Recommendation.java RecommendationEngine.java
```

Run with a randomly selected user:

```bash
java RecommendationEngine movies.csv ratings.csv
```

Run with a specific user ID:

```bash
java RecommendationEngine movies.csv ratings.csv 277
```

The third argument is optional.

## 📊 Example Results

The following recommendations were observed during a successful execution of the application.

**Selected user:** User 277

| Rank | Movie | Score |
|------|-------|-------|
| 1 | Executive Decision (1996) | 0.0813 |
| 2 | Down Periscope (1996) | 0.0716 |
| 3 | The River Wild (1994) | 0.0698 |
| 4 | Ransom (1996) | 0.0674 |
| 5 | Broken Arrow (1996) | 0.0667 |

These scores represent the algorithm's similarity-based ranking values, not predicted star ratings or calibrated probabilities.

Results may vary depending on the selected user and dataset.

## 🧠 Computer Science Skills Demonstrated

### Object-Oriented Design

The application separates responsibilities across movie, user, recommendation, and engine classes.

It uses constructors, private attributes, getter methods, and class-specific behavior to organize the implementation.

### Data Structures and Algorithms

The implementation demonstrates practical use of:

- Dynamic arrays and lists.
- Hash maps for keyed data access.
- Hash sets for mathematical set operations.
- Intersections and unions.
- Nested iteration and filtering.
- Custom sorting with lambda expressions.

### File Processing

The application reads external CSV files using Java I/O classes, converts string values into numerical data, and handles invalid numeric entries in the ratings dataset.

### Algorithmic Problem Solving

The project translates user preference information into a recommendation algorithm through similarity computation, candidate filtering, scoring, and ranking.

## ⚠️ Current Limitations

- The application uses a console interface rather than a GUI.
- Ratings are reduced to liked, disliked, or unclassified categories rather than preserving the full rating scale for scoring.
- The CSV parser uses basic comma splitting and may not handle every quoted CSV field correctly.
- Recommendation calculations repeatedly traverse users and their preference lists, which may affect performance on larger datasets.
- The `Recommendation` data class is defined but is not directly integrated into the current recommendation output.
- The project does not currently include automated recommendation-quality evaluation.

## 🔮 Potential Improvements

- [ ] Add automated unit tests for similarity calculations.
- [ ] Improve CSV parsing for quoted fields and embedded commas.
- [ ] Optimize user and movie lookups.
- [ ] Cache similarity calculations to reduce repeated work.
- [ ] Integrate the `Recommendation` class into the recommendation pipeline.
- [ ] Add an interactive user interface.
- [ ] Evaluate recommendation accuracy using held-out ratings.
- [ ] Compare alternative collaborative filtering approaches.

## 🎓 Academic Background

Originally developed for the **CSI2120/CSI2520 project during Winter 2025**.

This project provided practical experience in implementing recommendation algorithms, working with structured datasets, and applying Java programming concepts to a data-driven application.

## 👩🏽‍💻 Author

**Celia Ahui**

Computer Science & Mathematics Student  
University of Utah

[GitHub](https://github.com/celiaahui)

---

*Exploring how user preferences, similarity measures, and algorithms can turn data into personalized recommendations.*
