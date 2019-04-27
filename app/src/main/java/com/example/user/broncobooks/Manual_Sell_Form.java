package com.example.user.broncobooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Manual_Sell_Form.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Manual_Sell_Form#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Manual_Sell_Form extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Listing listingToEdit;

    String title, author, publishDate, publisher, language, subtitle, edition, binding, payment;
    int pages;
    double price;

    EditText bookTitle;
    EditText bookAuthors;
    EditText bookPublishDate;
    EditText bookPublisher;
    EditText bookLanguage;
    EditText bookSubtitle;
    EditText bookEdition;
    EditText bookBinding;
    EditText bookPrice;
    EditText bookPages;

    Spinner paymentChoice;

    Button submit;
    Button upload;

    ImageView pic1;
    ImageView pic2;
    ImageView pic3;
    ImageView pic4;
    ImageView pics[] = new ImageView[4];

    private OnFragmentInteractionListener mListener;
    private DatabaseReference dbReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public Manual_Sell_Form() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Manual_Sell_Form.
     */
    // TODO: Rename and change types and number of parameters
    public static Manual_Sell_Form newInstance(String param1, String param2) {
        Manual_Sell_Form fragment = new Manual_Sell_Form();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listingToEdit = null; //we want to set this to null ASAP
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual__sell__form, container, false);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.user.broncobooks",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            galleryAddPic();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if(imgFile.exists()) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                for(int i = 0; i < 4; i++) {
                    if(pics[i].getDrawable() == null) {
                        pics[i].setImageBitmap(imageBitmap);
                        return;
                    }
                }
                showToast("Maximum number of allowed pictures reached.");
            }
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            for(int i = 0; i < 4; i++) {
                if(pics[i].getDrawable() == null) {
                    pics[i].setImageBitmap(imageBitmap);
                    return;
                }
            }
            showToast("Maximum number of allowed pictures reached.");*/
        }
    }

    private String processAuthors(ArrayList<String> sList){
        String ans = "";
        if(sList != null){
            for(String s: sList){
                ans = ans + s + ", ";
            }
            if(sList.size() == 1){
                ans = ans.substring(0,ans.length()-2);
            }
        }
        return ans;
    }

    private int parsePaymentMethod(String method){
        if(method.equals("Check"))
            return 2;
        if(method.equals("Cash"))
            return 1;
        return 0;
    }

    //Purpose: To handle filling
    public void prefillFields(){
        if(getActivity().getCallingActivity()!=null){

            Intent intent = getActivity().getIntent();
            if(getActivity().getCallingActivity().getClassName().equals(ProfileDetail.class.getName())){
                TextView titleView = (TextView)getView().findViewById(R.id.textView);
                titleView.setText("Edit Listing");
                Toast.makeText(getActivity(),"Successfully completed source check :D",Toast.LENGTH_SHORT).show();
                listingToEdit = (Listing)intent.getSerializableExtra(ProfileDetail.LIST_TAG);

                String authorString = processAuthors(listingToEdit.textbook.authors);

                bookTitle.setText(listingToEdit.textbook.title);
                bookAuthors.setText(authorString);
                bookPublishDate.setText(listingToEdit.textbook.publishedDate);
                bookPublisher.setText(listingToEdit.textbook.publisher);
                bookLanguage.setText(listingToEdit.textbook.language);
                bookSubtitle.setText(listingToEdit.textbook.subtitle);
                bookEdition.setText(listingToEdit.textbook.edition);
                bookBinding.setText(listingToEdit.textbook.binding);
                bookPrice.setText(Double.toString(listingToEdit.price));
                bookPages.setText(Integer.toString(listingToEdit.textbook.pages));

                paymentChoice.setSelection(parsePaymentMethod(listingToEdit.paymentMethod));

                for(int x = 0; x < 4; x++){
                    StorageReference imageRef = storageReference.child("images").child(listingToEdit.id + "_" + Integer.toString(x)+".jpeg");
                    imageRef.getBytes(1*1024*1024)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    for(int i = 0; i < 4; i++) {
                                        if(pics[i].getDrawable() == null) {
                                            pics[i].setImageBitmap(bitmap);
                                            return;
                                        }
                                    }
                                }
                            });

                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Spinner spinner = (Spinner) getView().findViewById(R.id.bookPaymentInput);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.paymentMethod, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               payment = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });

        dbReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        bookTitle = (EditText) getView().findViewById(R.id.bookTitleInput);
        bookAuthors = (EditText) getView().findViewById(R.id.bookAuthorsInput);
        bookPublishDate = (EditText) getView().findViewById(R.id.bookPublishDateInput);
        bookPublisher = (EditText) getView().findViewById(R.id.bookPublisherInput);
        bookLanguage = (EditText) getView().findViewById(R.id.bookLanguageInput);
        bookSubtitle = (EditText) getView().findViewById(R.id.bookSubtitleInput);
        bookEdition = (EditText) getView().findViewById(R.id.bookEditionInput);
        bookBinding = (EditText) getView().findViewById(R.id.bookBindingInput);
        bookPrice = (EditText) getView().findViewById(R.id.bookPriceInput);
        bookPages = (EditText) getView().findViewById(R.id.bookPagesInput);

        pic1 = (ImageView) getView().findViewById(R.id.formPic1);
        pic2 = (ImageView) getView().findViewById(R.id.formPic2);
        pic3 = (ImageView) getView().findViewById(R.id.formPic3);
        pic4 = (ImageView) getView().findViewById(R.id.formPic4);
        pics[0] = pic1;
        pics[1] = pic2;
        pics[2] = pic3;
        pics[3] = pic4;

        paymentChoice = (Spinner) getView().findViewById(R.id.bookPaymentInput);

        prefillFields();

        submit = (Button) getView().findViewById(R.id.bookSubmitBtn);
        upload = (Button) getView().findViewById(R.id.bookPhotosBtn);

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message;

                message = "Are you sure you want to put this listing up for sale?";
                AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());

                builder.setTitle("Confirmation");
                builder.setMessage(message);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        title = bookTitle.getText().toString();
                        //Split authors from 1 string into an ArrayList
                        author = bookAuthors.getText().toString();
                        ArrayList<String> authors = new ArrayList<String>(Arrays.asList(author.split(",")));
                        for(int i = 0; i < authors.size(); i++) {
                            authors.set(i,authors.get(i).trim());
                        }


                        publishDate = bookPublishDate.getText().toString();
                        publisher = bookPublisher.getText().toString();
                        language = bookLanguage.getText().toString();
                        subtitle = bookSubtitle.getText().toString();
                        edition = bookEdition.getText().toString();
                        binding = bookBinding.getText().toString();

                        price = Double.valueOf(bookPrice.getText().toString());
                        pages = Integer.valueOf(bookPages.getText().toString());
                        Textbook newBook = new Textbook(title, subtitle, authors, publisher, publishDate, language, edition, pages, binding);

                        //Getting Epoch Time
                        int seconds = (int)(System.currentTimeMillis()/1000);

                        //Getting current User
                        FirebaseUser tempUser = FirebaseAuth.getInstance().getCurrentUser();
                        User user = new User(tempUser.getEmail(), tempUser.getDisplayName(), LoginActivity.userPhoneNumber);

                        //add to database
                        String key = "";
                        if(listingToEdit == null) {
                            key = dbReference.child("listings").push().getKey();
                            Toast.makeText(getView().getContext(),"Listing is null",Toast.LENGTH_LONG).show();
                        }
                        else{
                            key = listingToEdit.id;
                            Toast.makeText(getView().getContext(),"Listing is not null",Toast.LENGTH_LONG).show();
                        }
                        String listingPath = "/listings/" + key;
                        Listing newListing = new Listing(newBook, user, price, payment, seconds, key);

                        if(listingToEdit != null){
                            newListing.onSale = listingToEdit.onSale;
                            newListing.purchaseConfirmed = listingToEdit.purchaseConfirmed;
                            //Delete any images belonging to the pre-edited version
                            for(int x = 0; x < 4; x++){
                                StorageReference imageRef = storageReference.child("images").child(listingToEdit.id + "_" + Integer.toString(x)+".jpeg");
                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getView().getContext(),"Images successfully deleted",Toast.LENGTH_LONG);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getView().getContext(),"Images failed to delete",Toast.LENGTH_LONG);
                                    }
                                });

                            }
                        }
                        dbReference.child(listingPath).setValue(newListing);


                        //Prepare Images for upload
                        Bitmap bitmap;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        StorageReference path;
                        String imagePath;
                        for(int i = 0; i < 4; i++) {
                            if(pics[i].getDrawable() != null) {
                                pics[i].setDrawingCacheEnabled(true);
                                pics[i].buildDrawingCache();
                                bitmap = ((BitmapDrawable)pics[i].getDrawable()).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                                byte[] data = baos.toByteArray();
                                imagePath = "images/" + key + "_" + i + ".jpeg";
                                path =  storageReference.child(imagePath);
                                UploadTask uploadTask = path.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        // ...
                                    }
                                });
                            }
                        }

                        //Toast.makeText(getView().getContext(),"Listing added",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), TestingActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


    }


    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
