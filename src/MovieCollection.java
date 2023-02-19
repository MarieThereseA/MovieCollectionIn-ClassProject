import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class MovieCollection {
  private ArrayList<Movie> movies;
  private ArrayList<String> actors;
  private ArrayList<String> genres;
  private Scanner scanner;

  public MovieCollection(String fileName) {
    importMovieList(fileName);
    importActors();
    importGenres();
    scanner = new Scanner(System.in);
  }

  public ArrayList<Movie> getMovies() {
    return movies;
  }

  public void menu() {
    String menuOption = "";

    System.out.println("Welcome to the movie collection!");
    System.out.println("Total: " + movies.size() + " movies");

    while (!menuOption.equals("q")) {
      System.out.println("------------ Main Menu ----------");
      System.out.println("- search (t)itles");
      System.out.println("- search (k)eywords");
      System.out.println("- search (c)ast");
      System.out.println("- see all movies of a (g)enre");
      System.out.println("- list top 50 (r)ated movies");
      System.out.println("- list top 50 (h)igest revenue movies");
      System.out.println("- (q)uit");
      System.out.print("Enter choice: ");
      menuOption = scanner.nextLine();

      if (menuOption.equals("t")) {
        searchTitles();
      } else if (menuOption.equals("c")) {
        searchCast();
      } else if (menuOption.equals("k")) {
        searchKeywords();
      } else if (menuOption.equals("g")) {
        listGenres();
      } else if (menuOption.equals("r")) {
        listHighestRated();
      } else if (menuOption.equals("h")) {
        listHighestRevenue();
      } else if (menuOption.equals("q")) {
        System.out.println("Goodbye!");
      } else {
        System.out.println("Invalid choice!");
      }
    }
  }

  private void importMovieList(String fileName) {
    try {
      movies = new ArrayList<Movie>();
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = bufferedReader.readLine();

      while ((line = bufferedReader.readLine()) != null) {
        // get data from the columns in the current row and split into an array
        String[] movieFromCSV = line.split(",");

        /* TASK 1: FINISH THE CODE BELOW */
        // using the movieFromCSV array,
        // obtain the title, cast, director, tagline,
        // keywords, overview, runtime (int), genres,
        // user rating (double), year (int), and revenue (int)

        String title = movieFromCSV[0];
        String cast = movieFromCSV[1];
        String director = movieFromCSV[2];
        String tagline = movieFromCSV[3];
        String keywords = movieFromCSV[4];
        String overview = movieFromCSV[5];
        int runtime = Integer.parseInt(movieFromCSV[6]);
        String genres = movieFromCSV[7];
        double userRating = Double.parseDouble(movieFromCSV[8]);
        int year = Integer.parseInt(movieFromCSV[9]);
        int revenue = Integer.parseInt(movieFromCSV[10]);


        // create a Movie object with the row data:
        Movie movie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);

        // add the Movie to movies:
        movies.add(movie);

      }
      bufferedReader.close();
    } catch (IOException exception) {
      System.out.println("Unable to access " + exception.getMessage());
    }
  }

  private void importActors() {
    ArrayList<String> actors = new ArrayList<String>();
    String[] movieActors = movies.get(0).getCast().split("\\|");
    for (String actor : movieActors) {
      actors.add(actor);
    }
    for (int i = 1; i < movies.size(); i++) {
      movieActors = movies.get(i).getCast().split("\\|");

      for (int j = 0; j < movieActors.length; j++) {
        String actor = movieActors[j];
        if (actors.indexOf(actor) == -1) {
          actors.add(actor);
        }
      }
    }
    this.actors = actors;
  }

  private void importGenres() {
    ArrayList<String> genres = new ArrayList<String>();
    String[] movieGenres = movies.get(0).getGenres().split("\\|");
    for (String genre : movieGenres) {
      genres.add(genre);
    }
    for (int i = 1; i < movies.size(); i++) {
      movieGenres = movies.get(i).getGenres().split("\\|");

      for (int j = 0; j < movieGenres.length; j++) {
        String genre = movieGenres[j];
        if (genres.indexOf(genre) == -1) {
          genres.add(genre);
        }
      }
    }
    sortStringResults(genres);
    this.genres = genres;
  }

  private void sortResults(ArrayList<Movie> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      Movie temp = listToSort.get(j);
      String tempTitle = temp.getTitle();

      int possibleIndex = j;
      while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
  }

  private void sortStringResults(ArrayList<String> listToSort) {
    for (int j = 1; j < listToSort.size(); j++) {
      String temp = listToSort.get(j);

      int possibleIndex = j;
      while (possibleIndex > 0 && temp.compareTo(listToSort.get(possibleIndex - 1)) < 0) {
        listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
        possibleIndex--;
      }
      listToSort.set(possibleIndex, temp);
    }
  }

  private void displayMovieInfo(Movie movie) {
    System.out.println();
    System.out.println("Title: " + movie.getTitle());
    System.out.println("Tagline: " + movie.getTagline());
    System.out.println("Runtime: " + movie.getRuntime() + " minutes");
    System.out.println("Year: " + movie.getYear());
    System.out.println("Directed by: " + movie.getDirector());
    System.out.println("Cast: " + movie.getCast());
    System.out.println("Overview: " + movie.getOverview());
    System.out.println("User rating: " + movie.getUserRating());
    System.out.println("Box office revenue: " + movie.getRevenue());
  }

  private void searchTitles() {
    System.out.print("Enter a title search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String movieTitle = movies.get(i).getTitle();
      movieTitle = movieTitle.toLowerCase();

      if (movieTitle.indexOf(searchTerm) != -1) {
        //add the Movie objest to the results list
        results.add(movies.get(i));
      }
    }

    if (results.size() > 0) {
      // sort the results by title
      sortResults(results);

      // now, display them all to the user
      for (int i = 0; i < results.size(); i++) {
        String title = results.get(i).getTitle();

        // this will print index 0 as choice 1 in the results list; better for user!
        int choiceNum = i + 1;
        System.out.println("" + choiceNum + ". " + title);
      }

      System.out.println("Which movie would you like to learn more about?");
      System.out.print("Enter number: ");
      int choice = scanner.nextInt();
      scanner.nextLine();
      Movie selectedMovie = results.get(choice - 1);
      displayMovieInfo(selectedMovie);
      System.out.println("\n ** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    } else {
      System.out.println("\nNo movie titles match that search term!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    }
  }

  private void searchKeywords() {
    System.out.print("Enter a keyword search term: ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<Movie> results = new ArrayList<Movie>();

    // search through ALL movies in collection
    for (int i = 0; i < movies.size(); i++) {
      String keywords = movies.get(i).getKeywords();
      keywords = keywords.toLowerCase();

      if (keywords.indexOf(searchTerm) != -1) {
        //add the Movie object to the results list
        results.add(movies.get(i));
      }
    }

    if (results.size() > 0) {
      // sort the results by title
      sortResults(results);

      // now, display them all to the user
      for (int i = 0; i < results.size(); i++) {
        String title = results.get(i).getTitle();

        // this will print index 0 as choice 1 in the results list; better for user!
        int choiceNum = i + 1;
        System.out.println("" + choiceNum + ". " + title);
      }

      System.out.println("Which movie would you like to learn more about?");
      System.out.print("Enter number: ");
      int choice = scanner.nextInt();
      scanner.nextLine();
      Movie selectedMovie = results.get(choice - 1);
      displayMovieInfo(selectedMovie);
      System.out.println("\n ** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    } else {
      System.out.println("\nNo movie titles match that search term!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    }

  }

  private void searchCast() {
    System.out.print("Enter a person to search for (first or lastname): ");
    String searchTerm = scanner.nextLine();

    // prevent case sensitivity
    searchTerm = searchTerm.toLowerCase();

    // arraylist to hold search results
    ArrayList<String> results = new ArrayList<String>();

    // search through ALL actors in collection
    for (int i = 0; i < actors.size(); i++) {
      String actor = actors.get(i);
      if (actor.toLowerCase().indexOf(searchTerm) != -1) {
        //Tracker variable to see if the actor is already in the list
        boolean inList = false;
        for (int j = 0; j < results.size(); j++) {
          if (results.get(j).equals(actor)) {
            inList = true;
          }
        }
        if (!inList) {
          results.add(actor);
        }
      }
    }

    if (results.size() > 0) {
      // sort the results by title
      sortStringResults(results);

      // now, display them all to the user
      for (int i = 0; i < results.size(); i++) {
        String actor = results.get(i);

        // this will print index 0 as choice 1 in the results list; better for user!
        int choiceNum = i + 1;
        System.out.println("" + choiceNum + ". " + actor);
      }

      System.out.println("Which would you like to see all movies for?");
      System.out.print("Enter number: ");
      int choice = scanner.nextInt();
      scanner.nextLine();
      String selectedActor = results.get(choice - 1);

      //Creating list of movies that the actor is in
      ArrayList<Movie> moviesWithActor = new ArrayList<Movie>();
      for (int i = 0; i < movies.size(); i++) {
        String[] actors = movies.get(i).getCast().split("\\|");
        for (int j = 0; j < actors.length; j++) {
          if (actors[j].equals(selectedActor)) {
            moviesWithActor.add(movies.get(i));
          }
        }
      }

      sortResults(moviesWithActor);
      //Print Movies
      for (int i = 0; i < moviesWithActor.size(); i++) {
        String title = moviesWithActor.get(i).getTitle();

        // this will print index 0 as choice 1 in the results list; better for user!
        int choiceNum = i + 1;
        System.out.println("" + choiceNum + ". " + title);
      }

      System.out.println("Which movie would you like to learn more about?");
      System.out.print("Enter number: ");
      choice = scanner.nextInt();
      scanner.nextLine();
      Movie selectedMovie = moviesWithActor.get(choice - 1);
      displayMovieInfo(selectedMovie);

      System.out.println("\n ** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    } else {
      System.out.println("\nNo movie titles match that search term!");
      System.out.println("** Press Enter to Return to Main Menu **");
      scanner.nextLine();
    }
  }

  private void listGenres() {
    int choiceNum = 1;
    for (String genre : genres) {
      System.out.println(choiceNum + ". " + genre);
      choiceNum++;
    }

    System.out.println("Which would you like to see all movies for?");
    System.out.print("Enter number: ");
    int choice = scanner.nextInt();
    scanner.nextLine();
    String genre = genres.get(choice - 1);

    //Creating list of movies that have the genre
    ArrayList<Movie> moviesWithGenre = new ArrayList<Movie>();
    for (int i = 0; i < movies.size(); i++) {
      String[] movieGenres = movies.get(i).getGenres().split("\\|");
      for (int j = 0; j < movieGenres.length; j++) {
        if (movieGenres[j].equals(genre)) {
          moviesWithGenre.add(movies.get(i));
        }
      }
    }

    sortResults(moviesWithGenre);
    //Print Movies
    for (int i = 0; i < moviesWithGenre.size(); i++) {
      String title = moviesWithGenre.get(i).getTitle();

      // this will print index 0 as choice 1 in the results list; better for user!
      choiceNum = i + 1;
      System.out.println("" + choiceNum + ". " + title);
    }

    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");
    choice = scanner.nextInt();
    scanner.nextLine();
    Movie selectedMovie = moviesWithGenre.get(choice - 1);
    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }
  
  private void listHighestRated() {
    ArrayList<Movie> sortedMovies = movies;
    sortResults(sortedMovies);

    //Sort movies by rating from low to high
    for (int i = 1; i < sortedMovies.size(); i++){
      Movie tempMovie = sortedMovies.get(i);
      Double tempRating = sortedMovies.get(i).getUserRating();
      int possibleIndex = i;
      while (possibleIndex > 0 && tempRating < sortedMovies.get(possibleIndex - 1).getUserRating()) {
        sortedMovies.set(possibleIndex, sortedMovies.get(possibleIndex - 1));
        possibleIndex--;
      }
      sortedMovies.set(possibleIndex, tempMovie);
    }

    //Takes the top 50 ratings
    Movie[] fiftyMovies = new Movie[50];
    int idx = sortedMovies.size() - 1;
    for (int i = 0; i < fiftyMovies.length; i++){
      fiftyMovies[i] = sortedMovies.get(idx);
      idx --;
    }

    int choiceNum = 1;
    for (int i = 0; i < fiftyMovies.length; i++){
      System.out.println( choiceNum + ". " + fiftyMovies[i].getTitle() + ": " + fiftyMovies[i].getUserRating());
      choiceNum++;
    }


    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");
    int choice = scanner.nextInt();
    scanner.nextLine();
    Movie selectedMovie = fiftyMovies[choice - 1];
    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();
  }
  
  private void listHighestRevenue() {
    ArrayList<Movie> sortedMovies = movies;
    sortResults(sortedMovies);

    //Sort movies by rating from low to high
    for (int i = 1; i < sortedMovies.size(); i++){
      Movie tempMovie = sortedMovies.get(i);
      int tempRevenue = sortedMovies.get(i).getRevenue();
      int possibleIndex = i;
      while (possibleIndex > 0 && tempRevenue < sortedMovies.get(possibleIndex - 1).getRevenue()) {
        sortedMovies.set(possibleIndex, sortedMovies.get(possibleIndex - 1));
        possibleIndex--;
      }
      sortedMovies.set(possibleIndex, tempMovie);
    }

    //Takes the top 50 ratings
    Movie[] fiftyMovies = new Movie[50];
    int idx = sortedMovies.size() - 1;
    for (int i = 0; i < fiftyMovies.length; i++){
      fiftyMovies[i] = sortedMovies.get(idx);
      idx --;
    }

    int choiceNum = 1;
    for (int i = 0; i < fiftyMovies.length; i++){
      System.out.println( choiceNum + ". " + fiftyMovies[i].getTitle() + ": " + fiftyMovies[i].getRevenue());
      choiceNum++;
    }


    System.out.println("Which movie would you like to learn more about?");
    System.out.print("Enter number: ");
    int choice = scanner.nextInt();
    scanner.nextLine();
    Movie selectedMovie = fiftyMovies[choice - 1];
    displayMovieInfo(selectedMovie);

    System.out.println("\n ** Press Enter to Return to Main Menu **");
    scanner.nextLine();

  }
}