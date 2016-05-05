package com.purecode.househunt.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.purecode.househunt.R;
import com.purecode.househunt.model.House;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by pnagarjuna on 05/05/16.
 */
public class HouseListAdapter extends RecyclerView.Adapter<HouseViewHolder> {

    private List<House> houses;

    public HouseListAdapter(List<House> houses) {
        this.houses = houses;
    }

    public void addAll(List<House> houses) {
        this.houses.addAll(houses);
        notifyDataSetChanged();
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.housing_fragment_item, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder houseViewHolder, int position) {
        houseViewHolder.bind(houses.get(position));
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }
}

class HouseViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView rent;
    private TextView deposit;
    private TextView listingId;
    private ImageView listingImage;
    private LruCache<String, Bitmap> mMemoryCache;
    private View rootView;

    public HouseViewHolder(View itemView) {
        super(itemView);
        this.rootView = itemView;
        title = (TextView) itemView.findViewById(R.id.title);
        rent = (TextView) itemView.findViewById(R.id.rent);
        deposit = (TextView) itemView.findViewById(R.id.deposit);
        listingId = (TextView) itemView.findViewById(R.id.listingId);
        listingImage = (ImageView) itemView.findViewById(R.id.house_image);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            listingImage.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private String toReadableMoney(int price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("en", "IN"));
        String sPrice = formatter.format(price);
        if (price <= 0) return "Rs. ₹ --- ";
        return "Rs. ₹ " + sPrice;
    }

    public void bind(House house) {
        title.setText(house.getmTitle() + "");
        rent.setText("Rent " + toReadableMoney(house.getmRent()));
        deposit.setText("Deposit " + toReadableMoney(house.getmDeposit()));
        listingId.setText("Listing ID: " + house.getmListingId() + "");
        if (house.getImages().size() > 1) {
            Bitmap bitmap = getBitmapFromMemCache(house.getImages().get(1));
            if (bitmap == null) {
                new BitmapWorkerTask().execute(new String[]{house.getImages().get(1)});
            } else {
                listingImage.setImageBitmap(bitmap);
            }
        }
    }
}

