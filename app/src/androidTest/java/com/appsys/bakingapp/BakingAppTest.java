package com.appsys.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BakingAppTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void bakingAppTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipes_list),
                        withParent(allOf(withId(android.R.id.content),
                                withParent(withId(R.id.decor_content_parent)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.ingredients_of_recipe), withText("Ingredient")));
        appCompatButton.perform(scrollTo(), click());

        pressBack();

        ViewInteraction recyclerView2 = onView(
                withId(R.id.steps_of_recipe));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        ViewInteraction recyclerView3 = onView(
                withId(R.id.steps_of_recipe));
        recyclerView3.perform(actionOnItemAtPosition(4, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        pressBack();

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.recipes_list),
                        withParent(allOf(withId(android.R.id.content),
                                withParent(withId(R.id.decor_content_parent)))),
                        isDisplayed()));
        recyclerView4.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.ingredients_of_recipe), withText("Ingredient")));
        appCompatButton2.perform(scrollTo(), click());

        pressBack();

        ViewInteraction recyclerView5 = onView(
                withId(R.id.steps_of_recipe));
        recyclerView5.perform(actionOnItemAtPosition(4, click()));

        pressBack();
    }

}
