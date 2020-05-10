package bensalcie.likesyou.org.covi_19tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bensalcie.likesyou.org.covi_19tracker.models.Post;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class AffectedCountries extends AppCompatActivity {
    private static final  int GALLERY_REQUEST_CODE = 897;
    private ImageButton btnSend,btnGallery;
    private EditText etMessage;
    private ProgressBar pb;
    private Uri imageUri=null;
    private RelativeLayout chatHolder;

    String name,photo,email,id;
    private RecyclerView productsList;
    private DatabaseReference mPostsDatabase;

    private StorageReference mStorageReference;
    private Bitmap compressedImageFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);
        getSupportActionBar().setTitle("Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSend=findViewById(R.id.btnSend);
        btnGallery=findViewById(R.id.btnGallery);
        etMessage=findViewById(R.id.etMessage);
        pb=findViewById(R.id.progressBar);
        chatHolder=findViewById(R.id.chatHolder);
        mPostsDatabase= FirebaseDatabase.getInstance().getReference().child("COVI-APP").child("Posts");
        mStorageReference= FirebaseStorage.getInstance().getReference().child("COVI-APP").child("Post Images");
       name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        photo=getIntent().getStringExtra("photo");
        id=getIntent().getStringExtra("id");



        productsList=findViewById(R.id.productList);
        LinearLayoutManager gridLayoutManager=new LinearLayoutManager(this);
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);

        productsList.setLayoutManager(gridLayoutManager);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
            }
        });


       btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=etMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(message) && imageUri!=null){
                    pb.setVisibility(View.VISIBLE);

                    postData(name,photo,id,message,imageUri);

                }


            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST_CODE &&  resultCode==RESULT_OK){
            assert data != null;
            imageUri=data.getData();
            btnGallery.setImageURI(imageUri);
        }
    }
    private void postData(final String name, final String photo, final String id, final String message, Uri imageUri) {
       final StorageReference filePath=mStorageReference.child(imageUri.getLastPathSegment()+".jpg");

        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful())
                {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadLink=uri.toString();
                            DateFormat dateFormat=new SimpleDateFormat("HH:mm:ss ");
                            Date date=new Date();
                            String time=dateFormat.format(date);

                            String postKey=mPostsDatabase.push().getKey();

                            DatabaseReference newPost=mPostsDatabase.child(postKey);
                            Map<String,Object> post=new HashMap<>();
                            post.put("post_id",postKey);
                            post.put("message",message);
                            post.put("photo",downloadLink);
                            post.put("name", name);
                            post.put("person_photo", photo);
                            post.put("person_id", id);
                            post.put("time_posted",time);




                            newPost.updateChildren(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pb.setVisibility(View.GONE);
                                        toast("Your update is live");
                                        etMessage.setText("");

                                        btnGallery.setImageResource(R.drawable.galleryic);

                                    }else {
                                        pb.setVisibility(View.GONE);

                                        toast(task.getException().getMessage());
                                    }
                                }
                            });



                        }
                    });
                }else {
                    pb.setVisibility(View.GONE);

                    toast(task.getException().getMessage());
                }
            }
        });


            }

    private void toast(String message) {
        Toast.makeText(this, "Message:"+message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPostsDatabase.keepSynced(true);
        FirebaseRecyclerOptions<Post> options=new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(mPostsDatabase.limitToLast(35),Post.class)
                .build();
        FirebaseRecyclerAdapter<Post,productsViewHolder> adapter=new FirebaseRecyclerAdapter<Post, productsViewHolder>(options) {
            @NonNull
            @Override
            public productsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post,parent,false);


                return new productsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull productsViewHolder holder, int position, @NonNull final Post model) {
                holder.tvName.setText(model.getName());

                holder.tvMessage.setText(model.getMessage());
                holder.tvTime.setText(model.getTime_posted());
                Picasso.get().load(model.getPerson_photo()).resize(50, 50).placeholder(R.drawable.countryic).into(holder.profileImage);

                Picasso.get().load(model.getPhoto()).placeholder(R.drawable.countryic).into(holder.product_imageView);


                /*


                            post.put("views","0");
                            post.put("likes","0");
                 */
                checkViewsLikes(holder.tvLikes,holder.tvViews,model);
                mPostsDatabase.child(model.getPost_id()).child("views").child(model.getPerson_id()).setValue(1);
                holder.tvLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mPostsDatabase.child(model.getPost_id()).child("likes").child(model.getPerson_id()).setValue(1);

                    }
                });





            }

        };

        productsList.setAdapter(adapter);
        adapter.startListening();

        productsList.smoothScrollToPosition(adapter.getItemCount());


    }

    private void checkViewsLikes(final TextView tvLikes, final TextView tvViews, final Post model) {
        mPostsDatabase.child(model.getPost_id()).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int likes= (int) dataSnapshot.getChildrenCount();
                if (dataSnapshot.hasChild(model.getPerson_id())){
                    tvLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp,0,0,0);
                }
                tvLikes.setText(""+likes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mPostsDatabase.child(model.getPost_id()).child("views").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int views= (int) dataSnapshot.getChildrenCount();
                tvViews.setText(""+views);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class productsViewHolder extends RecyclerView.ViewHolder{
        private ImageView product_imageView;
        private CircleImageView profileImage;
        private TextView tvName,tvLikes,tvViews,tvTime,tvMessage;



        public productsViewHolder(@NonNull View itemView) {
            super(itemView);
            product_imageView=itemView.findViewById(R.id.photo);
            tvName=itemView.findViewById(R.id.tvName);
            tvLikes=itemView.findViewById(R.id.tvLikes);
            tvViews=itemView.findViewById(R.id.tvViews);
            tvTime=itemView.findViewById(R.id.tvTime);
            profileImage=itemView.findViewById(R.id.profilePic);
            tvMessage=itemView.findViewById(R.id.tvMessage);








        }
    }

}
