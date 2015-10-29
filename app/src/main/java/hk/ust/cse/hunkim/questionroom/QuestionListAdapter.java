package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * @author greg
 * @since 6/21/13
 * <p/>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class QuestionListAdapter extends FirebaseListAdapter<Question> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String roomName;
    MainActivity activity;

    public QuestionListAdapter(Query ref, Activity activity, int layout, String roomName) {
        super(ref, Question.class, layout, activity);

        // Must be MainActivity
        assert (activity instanceof MainActivity);

        this.activity = (MainActivity) activity;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view     A view instance corresponding to the layout we passed to the constructor.
     * @param question An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, final Question question) {
        final DBUtil dbUtil = activity.getDbutil();

        // Map a Chat object to an entry in our listview
        int echo = question.getEcho();
        Button likeButton = (Button) view.findViewById(R.id.like);
        likeButton.setText("" + echo);
        likeButton.setTextColor(Color.BLUE);

        likeButton.setTag(question.getKey()); // Set tag for button
        //likeButton.setSelected(dbUtil.contains(question.getKey()));
        //view.setTag(question.getKey());

        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();

                        Button questionDislikeButton = (Button) ((LinearLayout) view.getParent()).findViewById(R.id.dislike);
                        if(view.isSelected()){ // unlike when selected

                            m.updateLike((String) view.getTag(), -1);
                            view.setSelected(false);
                            //dbUtil.delete(view.getTag().toString());
                        }else if(questionDislikeButton.isSelected()){ // another dislike button is selected before

                            m.updateDislike((String) view.getTag(), -1);
                            m.updateLike((String) view.getTag(), 1);questionDislikeButton.setSelected(false);
                            view.setSelected(true);

                        }else{ //both like and dislike button are not selected before

                            m.updateLike((String) view.getTag(), 1);
                            //******************
                            //* From Matthew: Here,the code below, I had checked to ensure that I am retrieving the button with the tag same as the current question key
                            //*               For testing, you may create three new questions with the same like numbers,
                            //                then like the middle item and the problem I've mentioned will be appeared.
                            //      Button thisButton = (Button) ((RelativeLayout) view.getParent().getParent()).findViewWithTag(question.getKey()).findViewById(R.id.like);
                            //*     thisButton.setSelected(true);
                            //* but it turns out the same result as the code below
                            //*******************
                            view.setSelected(true);
                        }





                        //Button likebutton = (Button) likeParent.findViewById( R.id.echo );
                        //Button cancelButton = (Button) cancelParent.findViewById(R.id.id_name);
                        //Log.e("view tag", view.getTag().toString());
                        //Log.e("like tag",likebutton.getTag().toString());
                        //view.findViewById(R.id.echo)
                        // Button echoButton1 = (Button) view.findViewWithTag(view.getTag());
                        //likebutton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        //likeParent.setVisibility(View.GONE);
                    }
                }

        );

        int dislike = question.getDislike();
        Button dislikeButton = (Button) view.findViewById(R.id.dislike);
        dislikeButton.setText("" + dislike);
        dislikeButton.setTextColor(Color.RED);


        dislikeButton.setTag(question.getKey()); // Set tag for button
        dislikeButton.setSelected(dbUtil.contains(question.getKey()));

        dislikeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        Button questionLikeButton = (Button) ((LinearLayout) view.getParent()).findViewById(R.id.like);
                        if(view.isSelected()){ // undislike when selected
                            view.setSelected(false);
                            m.updateDislike((String) view.getTag(), -1);
                        }else if(questionLikeButton.isSelected()){ // another like button is selected before
                            questionLikeButton.setSelected(false);
                            m.updateLike((String) view.getTag(), -1);
                            view.setSelected(true);
                            m.updateDislike((String) view.getTag(), 1);
                        }else{ //both like and dislike button are not selected before
                            //From Matthew: same situation as the like implementation above
                            view.setSelected(true);
                            m.updateDislike((String) view.getTag(),1);
                        }
                    }
                }

        );

        String titleString = "";
        String msgString = "";

        question.updateNewQuestion();
        if (question.isLatest()) {
            ((TextView) view.findViewById(R.id.isNew)).setVisibility(view.VISIBLE);;
        }

        titleString += question.getHead();
        msgString += question.getWholeMsg();

        ((TextView) view.findViewById(R.id.head_desc)).setText(titleString);
        ((TextView) view.findViewById(R.id.onlymsg)).setText(msgString);

        // originally for adding an echo when click on the list text
        /*view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        MainActivity m = (MainActivity) view.getContext();
                                        m.updateLike((String) view.getTag());
                                    }
                                }
        );*/

        String timedisplay = DateUtils.getRelativeTimeSpanString(question.getTimestamp(), new Date().getTime(), 0, 262144).toString();
        ((TextView) view.findViewById(R.id.timedisplay)).setText(timedisplay);

        // check if we already clicked
        boolean clickable = !dbUtil.contains(question.getKey());

        /*echoButton.setClickable(clickable);
        dislikeButton.setClickable(clickable);
        echoButton.setEnabled(clickable);
        dislikeButton.setEnabled(clickable);*/
        //view.setClickable(clickable);


        // http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
        // grey out our button
        if (clickable) {
            //echoButton.getBackground().setColorFilter(null);
        } else {
            //echoButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }


        view.setTag(question.getKey());  // store key in the view
    }

    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels);
    }

    @Override
    protected void setKey(String key, Question model) {
        model.setKey(key);
    }
}