package abvgiet.library.libzy.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import abvgiet.library.libzy.R;
import abvgiet.library.libzy.User_Order_Sucess;
import abvgiet.library.libzy.model.Messages;
import abvgiet.library.libzy.model.UserInfo;

public class BookAdapter extends FirebaseRecyclerAdapter<Messages, BookAdapter.BookViewHolder> {

    ProgressBar progressBar;
    FirebaseDatabase rootnode;
    DatabaseReference reference;

    private String lastClickedItemPosition;


    public BookAdapter(@NonNull FirebaseRecyclerOptions<Messages> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, final int position, @NonNull Messages model) {

        //TextView


        holder.textView.setText(model.getName());
        holder.textView1.setText(model.getAuthor());
        holder.textView2.setText(model.getPages());




        Glide.with(holder.imageView.getContext()).load(model.getImage()).fitCenter().into(holder.imageView);






        //ImageView
       /* Glide.with(holder.imageView.getContext())
                .load(model.getImageUrl())
                .into(holder.imageView);*/
/*

        holder.itemView.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {



                Context context = v.getContext();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                context.startActivity(intent);







            }
        });
*/

    }

    // Use this method to get lastClickedItemPosition
    public String getLastClickedItemPosition() {
        return lastClickedItemPosition;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);

    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        //widgets
        ImageView imageView;
        TextView textView;
        TextView textView1;
        TextView textView2;
        Button Order_button,Submit_button,dialogButton;
        private Context context;
        ProgressBar progressBar;

        TextView User_name,Stream,Phone_no,Roll_no;




        public BookViewHolder(@NonNull final View itemView) {

            super(itemView);
            Submit_button = itemView.findViewById(R.id.submit_button);
            Order_button = itemView.findViewById(R.id.order_button);
            imageView = itemView.findViewById(R.id.item_book_img);
            textView = itemView.findViewById(R.id.item_book_title);
            textView1 = itemView.findViewById(R.id.item_book_author);
            textView2 = itemView.findViewById(R.id.item_book_pagesrev);
            progressBar = itemView.findViewById(R.id.progressBar);






            Order_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





                    context = itemView.getContext();

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.order_user_info);
                    dialog.setCancelable(false);

                    User_name = dialog.findViewById(R.id.name_user);
                    Stream = dialog.findViewById(R.id.stream);
                    Phone_no = dialog.findViewById(R.id.phone_number);
                    Roll_no = dialog.findViewById(R.id.roll_no);


                    textView = itemView.findViewById(R.id.item_book_title);


                    Button dialogButton = (Button) dialog.findViewById(R.id.submit_button);
                    ImageView imageView2 = (ImageView) dialog.findViewById(R.id.close_window);
                    imageView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialogButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            rootnode = FirebaseDatabase.getInstance();
                            reference = rootnode.getReference().child("Orders");

                            EditText emailId;

                            emailId = dialog.findViewById(R.id.email);


                            if (TextUtils.isEmpty(User_name.getText().toString()) ||TextUtils.isEmpty(Stream.getText().toString()) || TextUtils.isEmpty(Phone_no.getText().toString()) || TextUtils.isEmpty(Roll_no.getText().toString()) ){

                                Toast.makeText(context,"Please! Fill the Missing Details ",Toast.LENGTH_SHORT).show();

                            }else {
                                UserInfo userInfo = new UserInfo();

                                Random rand = new Random();
                                int n = rand.nextInt(5);
                                String w = "USER";

                                userInfo.setUser_name(User_name.getText().toString());
                                userInfo.setStream(Stream.getText().toString());
                                userInfo.setPhone_no(Phone_no.getText().toString());
                                userInfo.setRoll_no(Roll_no.getText().toString());
                                userInfo.setId(textView.getText().toString());


                                reference.child("abc").child(String.valueOf(n + w)).setValue(userInfo);

                                String visit_user_id = getRef(getAdapterPosition()).getKey();
                                Context context = v.getContext();
                                Intent intent = new Intent(context, User_Order_Sucess.class);
                                intent.putExtra("visit_user_id",visit_user_id);
                                context.startActivity(intent);

                            }




                            /*lastClickedItemPosition = getRef(getAdapterPosition()).getKey();*/



                           /* reference.push().setValue(userInfo);*/
/*
                            if (!data.getValue(User.class).getEmail().equals(email)) {
                                reference.child(reference.push().getKey()).setValue(new User(name, email));
                            }*/



                        }
                    });
                    dialog.show();


                   /* Context context = v.getContext();
                    Intent intent = new Intent(context, User_Order_Details.class);
                    context.startActivity(intent);*/


                }
            });




        }







    }}
