package com.appsys.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    private Integer mId;
    private String mTitle;
    private ArrayList<Ingredient> mIngredients = null;
    private ArrayList<Step> mSteps = null;
    private String mRecipeJSON = "{}";
    private Integer mServings;
    private String mImage;

    public Recipe() {}

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.mSteps = steps;
    }

    public Integer getServings() {
        return mServings;
    }

    public void setServings(Integer servings) {
        this.mServings = servings;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }
    
    public void setByJSON(JSONObject jsonRecipe) throws JSONException {
        mRecipeJSON = jsonRecipe.toString();

        setId(jsonRecipe.getInt("id"));
        setTitle(jsonRecipe.getString("name"));
        setServings(jsonRecipe.getInt("servings"));
        setImage(jsonRecipe.getString("image"));

        JSONArray jsonIngredients = jsonRecipe.getJSONArray("ingredients");
        int ingredientsCount = jsonIngredients.length();
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (int j = 0; j < ingredientsCount; j++) {
            JSONObject jsonIngredient = jsonIngredients.getJSONObject(j);
            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(jsonIngredient.getDouble("quantity"));
            ingredient.setIngredient(jsonIngredient.getString("ingredient"));
            ingredient.setMeasure(jsonIngredient.getString("measure"));
            ingredients.add(ingredient);
        }
        setIngredients(ingredients);

        JSONArray jsonSteps = jsonRecipe.getJSONArray("steps");
        int stepsCount = jsonSteps.length();
        ArrayList<Step> steps = new ArrayList<>();

        for (int j = 0; j < stepsCount; j++) {
            JSONObject jsonStep = jsonSteps.getJSONObject(j);
            Step step = new Step();
            step.setId(jsonStep.getInt("id"));
            step.setDescription(jsonStep.getString("description"));
            step.setShortDescription(jsonStep.getString("shortDescription"));
            step.setThumbnailURL(jsonStep.getString("thumbnailURL"));
            step.setVideoURL(jsonStep.getString("videoURL"));
            steps.add(step);
        }
        setSteps(steps);
    }

    public String getRecipeJSON() {
        return mRecipeJSON;
    }

    private Recipe(Parcel in) {
        mId = in.readByte() == 0x00 ? null : in.readInt();
        mTitle = in.readString();
        if (in.readByte() == 0x01) {
            mIngredients = new ArrayList<>();
            in.readList(mIngredients, Ingredient.class.getClassLoader());
        } else {
            mIngredients = null;
        }
        if (in.readByte() == 0x01) {
            mSteps = new ArrayList<>();
            in.readList(mSteps, Step.class.getClassLoader());
        } else {
            mSteps = null;
        }
        mServings = in.readByte() == 0x00 ? null : in.readInt();
        mImage = in.readString();
        mRecipeJSON = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mId);
        }
        dest.writeString(mTitle);
        if (mIngredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mIngredients);
        }
        if (mSteps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mSteps);
        }
        if (mServings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mServings);
        }
        dest.writeString(mImage);
        dest.writeString(mRecipeJSON);
    }

    @SuppressWarnings("unused")
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}