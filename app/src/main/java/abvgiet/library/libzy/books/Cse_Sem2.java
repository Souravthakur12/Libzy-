package abvgiet.library.libzy.books;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import abvgiet.library.libzy.R;
import abvgiet.library.libzy.adapter.BookAdapter;
import abvgiet.library.libzy.model.Messages;

public class Cse_Sem2 extends AppCompatActivity  {


    RecyclerView recyclerView;
    ProgressBar progressBar;



    //Variables
      private BookAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cse_sem2);

        recyclerView = findViewById(R.id.recycler_view);

        progressBar = findViewById(R.id.progressBar);
        FirebaseRecyclerOptions<Messages> options =
                new FirebaseRecyclerOptions.Builder<Messages>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Cse_Sem1"), Messages.class)
                        .build();


         adapter = new BookAdapter(options);
         recyclerView.setAdapter(adapter);
         adapter.notifyDataSetChanged();






        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



}
