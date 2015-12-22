package com.example.unreal_kz.randomizer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class QRGenerator extends AppCompatActivity implements View.OnClickListener {

    private static final int WIDTH = 220;
    private static final int BLACK = Color.BLACK;
    private static final int WHITE = Color.WHITE;
    private ImageView imgQRView;
    private Button btnGenerator;
    private String currentToken;
    private ParseQuery<ParseObject> parseObject,parseObjectParseQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        currentToken = ParseUser.getCurrentUser().getObjectId();
        parseObjectParseQuery = ParseQuery.getQuery("StayAlive");
        parseObject = ParseQuery.getQuery("StayAliveAchievement");
        parseObject.whereEqualTo("userId", currentToken);
        parseObjectParseQuery.whereEqualTo("userId",currentToken);
        Toast.makeText(QRGenerator.this,currentToken,Toast.LENGTH_SHORT).show();
        imgQRView = (ImageView) findViewById(R.id.imageView);
        btnGenerator = (Button) findViewById(R.id.btnGenerator);
        btnGenerator.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGenerator:
                try {
                    Bitmap bitmap = encodeAsBitmap(parseObjectParseQuery.getFirst().getObjectId());
                    imgQRView.setImageBitmap(bitmap);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    final byte[] byteArray = byteArrayOutputStream.toByteArray();
                    parseObject.findInBackground(new FindCallback<ParseObject>() {
                                                     @Override
                                                     public void done(List<ParseObject> list, ParseException e) {
                                                         if (e == null) {
                                                             for (ParseObject object:list){
                                                                 ParseFile file = new ParseFile("qr.png", byteArray);
                                                                 object.put("qrCode",file);
                                                                 object.saveInBackground();
                                                                 Toast.makeText(QRGenerator.this,""+byteArray,Toast.LENGTH_SHORT).show();
                                                             }
                                                         } else {
                                                             Toast.makeText(QRGenerator.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                         }
                                                     }
                                                 }
                    );
                } catch (WriterException e) {
                    e.printStackTrace();
                    Toast.makeText(QRGenerator.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(QRGenerator.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Bitmap encodeAsBitmap(String currentToken) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(currentToken,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }
}
