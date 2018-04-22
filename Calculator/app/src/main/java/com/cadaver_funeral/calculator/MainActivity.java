package com.cadaver_funeral.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    // 上のEditText
    private EditText numberInput1;

    // 下のEditText
    private EditText numberInput2;

    // 演算子選択用のSpinner
    private Spinner operatorSelector;

    // 計算結果表示用のTextView
    private TextView calcResult;

    // 上の「計算ボタン」を押した時のリクエストコード
    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;
    // 下の「計算ボタン」をタップした時のリクエストコード
    private static final int REQUEST_CODE_ANOTHER_CALC_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 上のEditText
        numberInput1 = (EditText) findViewById(R.id.numberInput1);
        // 上のEditTextの文字入力イベントを受け取る
        numberInput1.addTextChangedListener(this);

        // 下のEditText
        numberInput2 = (EditText) findViewById(R.id.numberInput2);
        // 下のEditTextの文字入力イベントを受け取る
        numberInput2.addTextChangedListener(this);

        // 演算子選択用のSpinner
        operatorSelector = (Spinner) findViewById(R.id.operatorSelector);

        // 計算結果表示用のTextView
        calcResult = (TextView) findViewById(R.id.calcResult);

        // 上の計算ボタン
        findViewById(R.id.calcButton1).setOnClickListener(this);
        // 下の計算ボタン
        findViewById(R.id.calcButton2).setOnClickListener(this);
        // 続けて計算するボタン
        findViewById(R.id.nextButton).setOnClickListener(this);
    }

    // 2つのEditTextに入力がされているかをチェックする
    private boolean checkEditTextInput() {
        // 入力内容を取得する
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();
        // 2つとも空文字列(あるいはnull)でなければ、true
        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // テキストが変更された後に呼ばれる。sは変更後のの内容で編集可能
        // 必要があれば計算を行い、結果を表示する
        refreshResult();
    }

    private void refreshResult() {
        if (checkEditTextInput()) {
            // 計算を行う
            int result = calc();

            // 計算結果用のTextViewを書き換える
            String resultText = getString(R.string.calc_result_text, result);
            calcResult.setText(resultText);
        } else {
            // どちらかが入力されていない状態の場合、計算結果用の表示をデフォルトに戻す
            calcResult.setText(R.string.calc_result_default);
        }
    }

    private int calc() {
        // 入力内容を取得してint型に変更する
        int number1 = Integer.parseInt(numberInput1.getText().toString());
        int number2 = Integer.parseInt(numberInput2.getText().toString());

        // Spinnerから選択中のindexを取得する
        int operator = operatorSelector.getSelectedItemPosition();

        // indexに応じて計算結果を返す
        switch (operator) {
            case 0: // 足し算
                return number1 + number2;
            case 1: // 引き算
                return number1 - number2;
            case 2: // 掛け算
                return number1 * number2;
            case 3: // 割り算
                return number1 / number2;
            default:
                // 通常発生しない
                throw new RuntimeException();
        }
    }

    @Override
    public void onClick(View v) {
        // タップされたViewのIDを取得する
        int id = v.getId();
        // IDごとに違う処理を行う
        switch (id) {
            case R.id.calcButton1:
                // 上の「計算」ボタンがタップされた時の処理
                Intent intent1 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_ANOTHER_CALC_1);
                break;
            case R.id.calcButton2:
                // 下の「計算」ボタンがタップされた時の処理
                Intent intent2 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_ANOTHER_CALC_2);
                break;
            case R.id.nextButton:
                // 「続けて計算する」ボタンがタップされた時の処理
                // 両方のEditTextに値が設定されていれば処理を行う
                if (checkEditTextInput()) {
                    // 計算する
                    int result = calc();
                    // 上のEditTextの値を書き換える
                    numberInput1.setText(String.valueOf(result));
                    // 計算し直して画面を更新する
                    refreshResult();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // startActivityResult()から戻ってきた時に呼ばれる
        super.onActivityResult(requestCode, resultCode, data);

        // 結果が「OK」ではない場合は何もしない
        if (resultCode != RESULT_OK) return;

        // 結果データセットを取り出す
        Bundle resultBundle = data.getExtras();

        // 結果データセットに所定のキーが含まれていない場合は何もしない
        if (!resultBundle.containsKey("result")) return;

        // 結果データから"result"キーに対応するint値を取り出す
        int result = resultBundle.getInt("result");


        if (requestCode == REQUEST_CODE_ANOTHER_CALC_1) {
            // 上の「計算」ボタンをタップした後戻ってきた場合
            numberInput1.setText(String.valueOf(result));
        } else if (requestCode == REQUEST_CODE_ANOTHER_CALC_2) {
            // 下の「計算」ボタンをタップした後戻ってきた場合
            numberInput2.setText(String.valueOf(result));
        }

        // 計算をし直して結果を表示する
        refreshResult();
    }
}
