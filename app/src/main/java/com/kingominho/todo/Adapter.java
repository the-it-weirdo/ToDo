package com.kingominho.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class Adapter extends PagerAdapter {

    private List<CategoryModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    private AdapterOnCardClickListener mAdapterOnCardClickListener;

    public Adapter(List<CategoryModel> models, Context context) {
        this.models = models;
        this.context = context;
        if (context instanceof AdapterOnCardClickListener) {
            mAdapterOnCardClickListener = (AdapterOnCardClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AdapterOnCardClickListener");
        }
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.category_item, container, false);

        ImageView categoryIcon;
        TextView categoryTitle, categoryTaskRemaining;
        CardView cardView;


        cardView = view.findViewById(R.id.cardView);
        categoryIcon = view.findViewById(R.id.categoryIcon);
        categoryTitle = view.findViewById(R.id.categoryTitle);
        categoryTaskRemaining = view.findViewById(R.id.categoryTaskRemaining);


        categoryIcon.setImageResource(models.get(position).getIcon());
        categoryTitle.setText(models.get(position).getCategoryTitle());
        categoryTaskRemaining.setText(models.get(position).getTaskRemaining());


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapterOnCardClickListener!=null)
                {
                    mAdapterOnCardClickListener.onCardClick(models.get(position).getCategoryTitle());
                }
                /*Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("param", models.get(position).getTitle());
                context.startActivity(intent);*/

                // finish();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public interface AdapterOnCardClickListener
    {
        void onCardClick(String param);
    }
}
