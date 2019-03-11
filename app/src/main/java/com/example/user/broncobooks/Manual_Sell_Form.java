package com.example.user.broncobooks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;


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


    Button submit;
    Button upload;

    ImageView pic1;
    ImageView pic2;
    ImageView pic3;
    ImageView pic4;

    private OnFragmentInteractionListener mListener;
    private DatabaseReference dbReference;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public Manual_Sell_Form() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(pic1.getDrawable() == null) {
                pic1.setImageBitmap(imageBitmap);
            } else if(pic2.getDrawable() == null) {
                pic2.setImageBitmap(imageBitmap);
            } else if(pic3.getDrawable() == null) {
                pic3.setImageBitmap(imageBitmap);
            } else if(pic4.getDrawable() == null) {
                pic4.setImageBitmap(imageBitmap);
            } else {
                showToast("Maximum number of allowed pictures reached.");
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
                int seconds = (int)System.currentTimeMillis()/1000;


                //Getting current User
                FirebaseUser tempUser = FirebaseAuth.getInstance().getCurrentUser();
                User user = new User(tempUser.getEmail(), tempUser.getDisplayName());

                Listing newListing = new Listing(newBook, user, price, payment, seconds);


                //add to database
                dbReference.child("listings").push().setValue(newListing);
                Toast.makeText(v.getContext(),"Listing added",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), TestingActivity.class);
                startActivity(intent);
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
