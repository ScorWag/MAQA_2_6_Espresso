package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;

@RunWith(AllureAndroidJUnit4.class)
public class SimpleEspressoTest {

    @Before
    public void registerIdlingResources() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.idlingResource);
    }

    @After
    public void unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.idlingResource);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testName() {
        String expectedText = "This is home fragment";
        ViewInteraction mainText = onView(withId(R.id.text_home));
        mainText.check(matches(isDisplayed()));
        mainText.check(matches(withText(expectedText)));
    }

    @Test
    public void testOpenSlideshow() {
        String expectedText = "This is slideshow fragment";
        ViewInteraction hamburgerButton = onView(isAssignableFrom(AppCompatImageButton.class));
        hamburgerButton.check(matches(isDisplayed())).perform(click());
        ViewInteraction slideshow = onView(withId(R.id.nav_slideshow));
        slideshow.check(matches(isDisplayed())).perform(click());
        ViewInteraction textSlideshow = onView(withId(R.id.text_slideshow));
        textSlideshow.check(matches(isDisplayed()));
        textSlideshow.check(matches(withText(expectedText)));
    }

    @Test
    public void testOpenGallery() {
        ViewInteraction hamburgerButton = onView(isAssignableFrom(AppCompatImageButton.class));
        hamburgerButton.check(matches(isDisplayed())).perform(click());
        ViewInteraction gallery = onView(withId(R.id.nav_gallery));
        gallery.check(matches(isDisplayed())).perform(click());
        ViewInteraction recycleView = onView(withId(R.id.recycle_view));
        recycleView.check(matches(isDisplayed()));
        ViewInteraction element = recycleView.perform(RecyclerViewActions
                .scrollTo(hasDescendant(withText("7"))));
        element.check(matches(isDisplayed()));
    }

    @Test
    public void testOpenGalleryWithTenItems() {
        ViewInteraction hamburgerButton = onView(isAssignableFrom(AppCompatImageButton.class));
        hamburgerButton.check(matches(isDisplayed())).perform(click());
        ViewInteraction gallery = onView(withId(R.id.nav_gallery));
        gallery.check(matches(isDisplayed())).perform(click());
        ViewInteraction recycleView = onView(withId(R.id.recycle_view));
        recycleView.check(matches(isDisplayed()));
        recycleView.check(matches(CustomViewMatcher.recyclerViewSizeMatcher(10)));
    }

    @Test
    public void testGalleryListIsRecyclerView() {
        ViewInteraction hamburgerButton = onView(isAssignableFrom(AppCompatImageButton.class));
        hamburgerButton.check(matches(isDisplayed())).perform(click());
        ViewInteraction gallery = onView(withId(R.id.nav_gallery));
        gallery.check(matches(isDisplayed())).perform(click());
        ViewInteraction recycleView = onView(withId(R.id.recycle_view));
        recycleView.check(matches(isDisplayed()));
        recycleView.check(CustomViewAssertions.isRecyclerView());
    }

    @Test
    public void testPressHomeInHamburgerMenu() {
        ViewInteraction mainText = onView(withId(R.id.text_home));
        mainText.check(matches(isDisplayed()));
        ViewInteraction hamburgerButton = onView(isAssignableFrom(AppCompatImageButton.class));
        hamburgerButton.check(matches(isDisplayed())).perform(click());
        ViewInteraction home = onView(withId(R.id.nav_home));
        home.check(matches(isDisplayed())).perform(click());
        mainText.check(matches(isDisplayed()));
        home.check(matches(not(isDisplayed())));
    }

    @Test
    public void testOpenRightMenu() {
        ViewInteraction rightMenu = onView(withParent(withParent(withId(R.id.toolbar))));
        rightMenu.check(matches(isDisplayed()));
        Intents.init();
        rightMenu.perform(click());
        ViewInteraction settingsButton = onView(allOf(withId(R.id.title),
                withParent(withParent(withId(R.id.content)))));
        settingsButton.check(matches(isDisplayed()));
        settingsButton.perform(click());
        intended(hasData("https://google.com"));
        intended(hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }

}