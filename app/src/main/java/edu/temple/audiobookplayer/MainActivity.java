package edu.temple.audiobookplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedInterface {

    FragmentManager fragmentManager;
    boolean twoPane;
    BookDetailsFragment bookDetailsFragment;
    BookList myBooks = new BookList();
    Book selectedBook;
    String[] titles = {"Capitalist Realism: Is There No Alternative?", "The Conquest of Bread", "Dune",
        "The Phenomenology of Spirit", "Infinite Jest", "Pale Fire",
        "Ulysses"
    };
    String[] authors = {"Mark Fisher", "Peter Kropotkin", "Frank Herbert",
        "Georg Wilhelm Friedrich Hegel", "David Foster Wallace", "Vladimir Nabokov",
        "James Joyce"
    };

    private final String SELECTED_BOOK = "selectedBook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i < titles.length; i++){
        myBooks.add(new Book(titles[i], authors[i]));
    }

        if(savedInstanceState != null){
            selectedBook = savedInstanceState.getParcelable(SELECTED_BOOK);
        }
        twoPane = findViewById(R.id.container2) != null;

        fragmentManager = getSupportFragmentManager();

        Fragment fragment;
        fragment = fragmentManager.findFragmentById(R.id.container1);

        if(fragment instanceof BookDetailsFragment){
            fragmentManager.popBackStack();
        } else if (!(fragment instanceof BookListFragment)){
            fragmentManager.beginTransaction().add(R.id.container1, BookListFragment.newInstance(myBooks)).commit();
        }

        if(selectedBook == null){
            bookDetailsFragment = new BookDetailsFragment();
        } else {
            bookDetailsFragment = BookDetailsFragment.newInstance(selectedBook);
        }

        if(twoPane){
            fragmentManager.beginTransaction().replace(R.id.container2, bookDetailsFragment).commit();
        } else if (selectedBook != null) {
            fragmentManager.beginTransaction().replace(R.id.container1, bookDetailsFragment).addToBackStack(null).commit();
        }
    }
    @Override
    public void bookSelected(int index){
        selectedBook = myBooks.get(index);
        if(twoPane){
            bookDetailsFragment.showBook(selectedBook);
        } else {
            fragmentManager.beginTransaction().replace(R.id.container1, BookDetailsFragment.newInstance(selectedBook)).addToBackStack(null).commit();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_BOOK, selectedBook);
    }

    @Override
    public void onBackPressed(){
        selectedBook = null;
        super.onBackPressed();
    }
}