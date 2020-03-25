package io.github.japanesepractice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    int NUM_OF_WORDS = 10;
    String textInKana = "";
    String textInRomanji = "";
    final String[][] H_REPLACEMENTS = new String[][]{
            {"あ", "い", "う", "え", "お", "か", "が", "き", "ぎ", "く", "ぐ", "け", "げ", "こ", "ご", "さ", "ざ", "し", "じ", "す", "ず",
                    "せ", "ぜ", "そ", "ぞ", "た", "だ", "ち", "ぢ", "つ", "づ", "て", "で", "と", "ど", "な", "に", "ぬ", "ね", "の", "は",
                    "ば", "ぱ", "ひ", "び", "ぴ", "ふ", "ぶ", "ぷ", "へ", "べ", "ぺ", "ほ", "ぼ", "ぽ", "ま", "み", "む", "め", "も", "や",
                    "ゆ", "よ", "ら", "り", "る", "れ", "ろ", "わ", "ゐ", "ゑ", "を", "ん", "ゔ"
            },
            {"a", "i", "u", "e", "o", "ka", "ga", "ki", "gi", "ku", "gu", "ke", "ge", "ko", "go", "sa", "za", "shi", "ji", "su", "zu",
                    "se", "ze", "so", "zo", "ta", "da", "chi", "dzi", "tsu", "dzu", "te", "de", "to", "do", "na", "ni", "nu", "ne",
                    "no", "ha", "ba", "pa", "hi", "bi", "pi", "fu", "bu", "pu", "he", "be", "pe", "ho", "bo", "po", "ma", "mi", "mu",
                    "me", "mo", "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro", "wa", "wi", "we", "wo", "n", "vu"
            }
    };
    final String[][] H_SMALL_FORMS = new String[][]{
            {"ぁ", "ぃ", "ぅ", "ぇ", "ぉ", "ゃ", "ゅ", "ょ", "ゎ", "ゕ", "ゖ"},
            {"a", "i", "u", "e", "o", "ya", "yu", "yo", "wa", "ka", "ke"}
    };
    final String[][] K_REPLACEMENTS = new String[][]{
            {"ア", "イ", "ウ", "エ", "オ", "カ", "ガ", "キ", "ギ", "ク", "グ", "ケ", "ゲ", "コ", "ゴ", "サ", "ザ", "シ", "ジ", "ス", "ズ",
                    "セ", "ゼ", "ソ", "ゾ", "タ", "ダ", "チ", "ヂ", "ツ", "ヅ", "テ", "デ", "ト", "ド", "ナ", "ニ", "ヌ", "ネ", "ノ", "ハ",
                    "バ", "パ", "ヒ", "ビ", "ピ", "フ", "ブ", "プ", "ヘ", "ベ", "ペ", "ホ", "ボ", "ポ", "マ", "ミ", "ム", "メ", "モ", "ヤ",
                    "ユ", "ヨ", "ラ", "リ", "ル", "レ", "ロ", "ワ", "ヰ", "ヱ", "ヲ", "ン", "ヴ", "ヷ", "ヸ", "ヹ", "ヺ"
            },
            {"a", "i", "u", "e", "o", "ka", "ga", "ki", "gi", "ku", "gu", "ke", "ge", "ko", "go", "sa", "za", "shi", "ji", "su", "zu",
                    "se", "ze", "so", "zo", "ta", "da", "chi", "dzi", "tsu", "dzu", "te", "de", "to", "do", "na", "ni", "nu", "ne", "no",
                    "ha", "ba", "pa", "hi", "bi", "pi", "fu", "bu", "pu", "he", "be", "pe", "ho", "bo", "po", "ma", "mi", "mu", "me",
                    "mo", "ya", "yu", "yo", "ra", "ri", "ru", "re", "ro", "wa", "wi", "we", "wo", "n", "vu", "va", "vi", "ve", "vo"
            }
    };
    final String[][] K_SMALL_FORMS = new String[][]{
            {"ァ", "ィ", "ゥ", "ェ", "ォ", "ャ", "ュ", "ョ", "ヮ", "ヵ", "ヶ"},
            {"a", "i", "u", "e", "o", "ya", "yu", "yo", "wa", "ka", "ke"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton submitButton = findViewById(R.id.fab);
        final TextView textToTranscribe = findViewById(R.id.textToTranscribe);
        final EditText input = findViewById(R.id.search_edit);
        final ToggleButton toggle = findViewById(R.id.toggleButton);

        final boolean[] typingResponse = {true};
        final boolean[] isHiragana = {false};
        toggle.setTextOn(getString(R.string.switch_hiragana));
        toggle.setTextOff(getString(R.string.switch_katakana));
        toggle.setChecked(false);
        toggle.setHighlightColor(Color.WHITE);
        try {
            generateNewPrompt(textToTranscribe, input, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typingResponse[0]) {
                    checkResponse(textToTranscribe, input);
                    input.setVisibility(View.INVISIBLE);
                    hideKeyboardFrom(getBaseContext(), view);
                } else {
                    try {
                        generateNewPrompt(textToTranscribe, input, isHiragana[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    input.setVisibility(View.VISIBLE);
                    showKeyboard(input, getBaseContext());
                }
                typingResponse[0] = !typingResponse[0];
                input.setText("");
            }
        });

        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setRawInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitButton.performClick();
                    return true;
                }
                return false;
            }
        });

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHiragana[0] = !isHiragana[0];
                toggle.setChecked(isHiragana[0]);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkResponse(TextView textToTranscribe, EditText response) {

        //TODO: highlight wrong chars only
        /*
         * look through all wrong chars
         * */
        //-------------------------
        String[] correctAnswer = textInRomanji.split(" ");
        String[] userAnswer = response.getText().toString().toLowerCase().split(" ");
        String[] wordsInKana = textInKana.replace("     ", " ").split(" ");
        System.out.println(textInRomanji);
        ArrayList<Integer> wrongAnswerIndexes = new ArrayList<>();
        for (int i = 0; i < Math.min(correctAnswer.length, userAnswer.length); i++) {
            if (!userAnswer[i].equals(correctAnswer[i])) {
                wrongAnswerIndexes.add(i);
            }
        }
        ArrayList<Integer> overallRangesToColor = new ArrayList<>();
        StringBuilder textToDisplay = new StringBuilder();
        for (int i = 0; i < wordsInKana.length; i++) {
            if (wrongAnswerIndexes.contains(i)) {
                overallRangesToColor.add(textToDisplay.length());
                overallRangesToColor.add(textToDisplay.length() + wordsInKana[i].length());
            }
            textToDisplay.append(wordsInKana[i]).append("  /  ");
        }
        textToDisplay.delete(textToDisplay.length() - 5, textToDisplay.length());
        textToDisplay.append('\n');
        for (int i = 0; i < userAnswer.length; i++) {
            if (wrongAnswerIndexes.contains(i)) {
                overallRangesToColor.add(textToDisplay.length());
                overallRangesToColor.add(textToDisplay.length() + userAnswer[i].length());
            }
            textToDisplay.append(userAnswer[i]).append(" ");
        }
        textToTranscribe.setText(textToDisplay, TextView.BufferType.SPANNABLE);
        Spannable textSpan = (Spannable) textToTranscribe.getText();
        for (int i = 0; i < overallRangesToColor.size(); i += 2) {
            textSpan.setSpan(new ForegroundColorSpan(Color.RED), overallRangesToColor.get(i), overallRangesToColor.get(i + 1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (int i = 0; i < textToDisplay.length(); i++) {
            if (textToDisplay.charAt(i) == '/') {
                textSpan.setSpan(new ForegroundColorSpan(Color.rgb(220, 220, 220)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void generateNewPrompt(TextView textToTranscribe, EditText response, boolean isHiragana) throws IOException {
        int[] lines = new int[NUM_OF_WORDS];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = rand(0, 2890);
        }
        Arrays.sort(lines);
        System.out.println(Arrays.toString(lines));
        StringBuilder stringBuilder = new StringBuilder();
        String textInCurrentLine;
        int currentLineNum = 0;
        int indexOfLineNumToGet = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(isHiragana ? R.raw.hiragana : R.raw.katakana)));
        while ((textInCurrentLine = reader.readLine()) != null && indexOfLineNumToGet < lines.length) {
            if (lines[indexOfLineNumToGet] <= currentLineNum) {
                stringBuilder.append(textInCurrentLine).append("     ");
                indexOfLineNumToGet++;
            }
            currentLineNum++;
        }
        reader.close();
        textInKana = stringBuilder.toString();
        textInRomanji = kanaToRomanji(textInKana, isHiragana);
        String textToDisplay = textInKana.replace("     ", "  /  ").substring(0, textInKana.length() - 5);
        textToTranscribe.setText(textToDisplay, TextView.BufferType.SPANNABLE);
        Spannable textSpan = (Spannable) textToTranscribe.getText();
        for (int i = 0; i < textToDisplay.length(); i++) {
            if (textToDisplay.charAt(i) == '/') {
                textSpan.setSpan(new ForegroundColorSpan(Color.rgb(220, 220, 220)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        response.setText("");
    }

    public String kanaToRomanji(String kana, boolean isHiragana) {
        String[][] replacements = isHiragana ? H_REPLACEMENTS : K_REPLACEMENTS;
        String[][] smallForms = isHiragana ? H_SMALL_FORMS : K_SMALL_FORMS;
        String smallTsuSymbol = isHiragana ? "っ" : "ッ";
        for (int i = 0; i < replacements[0].length; i++) {
            kana = kana.replace(replacements[0][i], replacements[1][i]);
        }
        for (int i = 0; i < smallForms[0].length; i++) {
            while (kana.contains(smallForms[0][i])) {
                kana = kana.substring(0, kana.indexOf(smallForms[0][i]) - 1) + smallForms[1][i] + kana.substring(kana.indexOf(smallForms[0][i]) + 1);
            }
        }
        kana = kana.replace("jy", "j");
        kana = kana.replace("shy", "sh");
        int indexOfSmallTsu;
        while (kana.contains(smallTsuSymbol)) {
            indexOfSmallTsu = kana.indexOf(smallTsuSymbol);
            if (indexOfSmallTsu + 1 < kana.length() && kana.charAt(indexOfSmallTsu + 1) != ' ') {
                kana = kana.substring(0, indexOfSmallTsu) + kana.charAt(indexOfSmallTsu + 1) + kana.substring(indexOfSmallTsu + 1);
            } else {
                kana = kana.replaceFirst(smallTsuSymbol, "");
            }
        }
        while (kana.contains("ー")) {
            if (kana.indexOf('ー') > 0 && kana.charAt(kana.indexOf('ー') - 1) != ' ') {
                kana = kana.substring(0, kana.indexOf('ー')) + kana.charAt(kana.indexOf('ー') - 1) + kana.substring(kana.indexOf('ー') + 1);
            } else {
                kana = kana.replaceFirst("ー", "");
            }
        }
        return kana.replace("     ", " ");
    }

    //https://stackoverflow.com/a/17789187/12861567
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //https://stackoverflow.com/a/34306465/12861567
    public static void showKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public int rand(int a, int b) {
        return (int) (Math.random() * (Math.abs(a - b) + 1)) + Math.min(a, b);
    }
}
