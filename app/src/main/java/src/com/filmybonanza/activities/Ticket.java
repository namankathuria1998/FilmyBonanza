package src.com.filmybonanza.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import src.com.filmybonanza.model.BookedEvent;
import src.com.filmybonanza.R;
import src.com.filmybonanza.singleton.DependencyInjection;

public class Ticket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);

        final Intent intent=getIntent();
        String json = intent.getStringExtra("key");
        String fee = intent.getStringExtra("fee");
        String seatsSelected = intent.getStringExtra("SeatsSelected");
        BookedEvent bookedEvent  =  DependencyInjection.getGson().fromJson(json, BookedEvent.class);

        ImageView iv = findViewById(R.id.image);
        Picasso.get().load(bookedEvent.getPoster()).into(iv);
        TextView title=findViewById(R.id.title);
        title.setText(bookedEvent.getTitle());
        TextView evetid = findViewById(R.id.id);
        evetid.setText("Event Id: "+bookedEvent.getEventId());
        TextView date = findViewById(R.id.date);
        date.setText("Event Date: "+bookedEvent.getDate());
        TextView timings = findViewById(R.id.timings);
        timings.setText("Show Timings: "+bookedEvent.getTimings());
        TextView location = findViewById(R.id.location);
        location.setText("Location of the event: "+bookedEvent.getLocation());
        TextView totalfee = findViewById(R.id.totalfee);
        totalfee.setText("Total Fee: "+ fee);
        TextView seats = findViewById(R.id.seatsSelected);
        seats.setText("Seats Selected :- "+seatsSelected);
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToStr = format.format(today);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat mydate = new SimpleDateFormat("HH:mm a");
        mydate.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = mydate.format(currentLocalTime);
        Random rand = new Random();
        double rand_dub = rand.nextDouble();

        String uniquekey = "EventId :- "+bookedEvent.getEventId() + " . UserId :- "+bookedEvent.getUid() +
                " . Timestamp :- "+dateToStr + " " + localTime + " . RandomNo :- " + rand_dub;

        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(uniquekey, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView barcode=findViewById(R.id.barcode);
            barcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.downloadmenu, menu); return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.download:
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                try {

                    String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "movieimage" + ".jpg";
                    View v1 = getWindow().getDecorView().getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    File imageFile = new File(mPath);
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    imageToPDF();

                } catch (Throwable e) {
                    Log.e("TAG", e.toString());
                }
                break;
        }
        return true;
    }

    public void imageToPDF() throws FileNotFoundException {
        try {

            Document document=new Document() ;
            String dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/MoviePDF.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + "/"+ "movieimage.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MoviePDF.pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } catch (Exception e) {
            Log.e("pdf not created", e.toString() );
        }
    }
}
