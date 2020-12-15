package com.example.niger.nigerangyidong_150271176_co3320;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.L;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    ImageView imagePreview  ;
    private FoodArrayAdapter resultsArrayAdapter;
    private ListView resultsListView;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;
    List<String[]> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the navigation bar to be able to customize menu
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Find view for respective sections
        //resultEditView = findViewById(R.id.resultEt);
        imagePreview = findViewById(R.id.imageIv);

        //Populate List View
        resultsListView = findViewById(R.id.list);
        //Create an empty view for when there are no data in database
        View emptyView = findViewById(R.id.empty_view);
        resultsListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row
        resultsArrayAdapter = new FoodArrayAdapter(getApplicationContext(), R.layout.list_item);
        resultsListView.setAdapter(resultsArrayAdapter);

        //Hide keyboard when app starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //camera permission
        cameraPermission = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Access and stream res/raw/data.csv
        //Load into FoodArray with CSVReader class
        InputStream inputStream = getResources().openRawResource(R.raw.data);
        CSVReader csv = new CSVReader(inputStream);
        foodList = csv.read();
    }

    //toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    //handle menu item clicks, response accordingly to whichever option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_image:
                showImageImportDialog();
                break;
            case R.id.action_database:
                Intent intent = new Intent(getApplicationContext(), TableActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        //items to display in dialog
        String[] items = {" Camera", " Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //set title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //camera option clicked
                    if(!checkCameraPermission()){
                        //camera permission not allowed, request it
                        requestCameraPermission();
                    }else{
                        //camera permission allowed
                        pickCamera();
                    }
                }
                if(which == 1){
                    //gallery option clicked
                    if(!checkStoragePermission()){
                        //storage permission not allowed
                        requestStoragePermission();
                    }else{
                        //storage permission allowed
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show(); //show dialog
    }

    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickCamera() {
        //take image from camera, save to storage
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Scanned_Label"); // title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text"); //description of picture
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean storageResult = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return storageResult;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickGallery() {
        //take image from storage
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    //handle permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }else{
                        Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT);
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAccepted){
                        pickGallery();
                    }else{
                        Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT);
                    }
                }
                break;
        }
    }

    //handle image result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image from gallery and enable grid lines
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image from camera
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
        }
        //get cropped image
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri(); //get image uri
                //set image to image view
                imagePreview.setImageURI(resultUri);

                //get drawable bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imagePreview.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if(!recognizer.isOperational()){
                    Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();
                }else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    String lines = "";
                    String words = "";
                    ArrayList<Textholder> textholder = new ArrayList<Textholder>();
                    ArrayList<Textholder> textholder1 = new ArrayList<Textholder>();
                    for(int index = 0; index < items.size(); index ++){
                        int key = items.keyAt(index);
                        TextBlock textBlock = items.valueAt(index);
                        if(textBlock != null){ //if there is a block, get its components
                            for(Text line : textBlock.getComponents()){ //go through each line in the block
                                float topLine = line.getBoundingBox().top;
                                Textholder lineHolder = new Textholder(topLine, line.getValue());
                                textholder1.add(lineHolder);
                                for(Text element: line.getComponents()) {
                                    float top = element.getBoundingBox().top; //record the line's box and coordinates
                                    Textholder th = new Textholder(top, element.getValue());
                                    textholder.add(th);
                                }
                            }
                        }
                    }
                    if(items.size() == 0){ //if no text found
                        Toast.makeText(this, "No Text Found", Toast.LENGTH_SHORT).show();
                    }else{
                        Collections.sort(textholder);
                        for(int i = 0; i < textholder1.size(); i++){
                            Textholder t = textholder1.get(i);
                            lines = lines + t.getText();
                        }

                        //Version 3
                        lines = lines.replaceAll("\\(",",").replaceAll("\\)",",").replaceAll("\\.",",");
                        String [] food = lines.split(",");
                        String [] temp;
                        double [] dist = new double[food.length];
                        double threshold = 0.52;
                        List<String[]> resultList = new ArrayList<String[]>();

                        //Loop through scanned results
                        for(int z = 0; z < food.length;z++){
                            String [] resultData = new String[3];
                            resultList.add(null);

                            for(int x = 0; x < foodList.size(); x++ ){
                                temp = foodList.get(x);
                                double value = similarity(food[z].trim(),temp[2].trim());
                                if(value > threshold){
                                    if(value > dist[z]) {
                                        dist[z] = value;
                                        resultData[0] = temp[0]; //add type of uses to StringArray resultsData
                                        resultData[1] = temp[1]; //add purpose
                                        resultData[2] = temp[2]; //add name
                                        resultList.set(z, resultData);
                                    }
                                    if(value == 1.000){ //if exact match found, move to next ingredient from the scanned list
                                        System.out.println(temp[2] + " matched");
                                        break ;
                                    }
                                }
                            }
                        }
                        //remove all results that yield nothing
                        resultList.removeAll(Collections.singleton(null));
                        for(String [] resultData : resultList){
                            resultsArrayAdapter.add(resultData);
                            System.out.println(Arrays.toString(resultData));
                        }
                        //resultEditView.setText(lines);
                        recognizer.release();
                    }
                }
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //if there is any error, show it
                Exception error = result.getError();
                Toast.makeText(this,""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double)longerLength;
    }

    // Levenshtein Edit Distance
    // Source: https://ideone.com/oOVWYj
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static void printSimilarity(String s, String t) {
        System.out.println(String.format("%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
    }

}

