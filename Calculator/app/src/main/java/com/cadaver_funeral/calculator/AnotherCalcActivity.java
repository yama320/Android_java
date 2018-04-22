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

public class AnotherCalcActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    // 上のEditText
    private EditText numberInput1;

    // 下のEditText
    private EditText numberInput2;

    // 演算子選択用のSpinner
    private Spinner operatorSelector;

    // 計算結果表示用のTextView
    private TextView calcResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 上のEditText
        numberInput1 = (EditText) findViewById(R.id.numberInput1);
        // 上のEditTextの文字入力イベントを受け取る
        numberInput1.addTextChangedListener((TextWatcher) this);

        // 下のEditText
        numberInput2 = (EditText) findViewById(R.id.numberInput2);
        // 下のEditTextの文字入力イベントを受け取る
        numberInput2.addTextChangedListener(this);

        // 演算子選択用のSpinner
        operatorSelector = (Spinner) findViewById(R.id.operatorSelector);

        // 計算結果表示用のTextView
        calcResult = (TextView) findViewById(R.id.calcResult);

        // 「戻る」ボタン
        findViewById(R.id.backButton).setOnClickListener(this);
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
        // 「戻る」ボタンがタップされた時
        // どちらかのEditTextに値が入っていない場合
        if (!checkEditTextInput()) {
            // キャンセルとみなす
            setResult(RESULT_CANCELED);
        } else {
            // 計算結果
            int result = calc();

            // インテントを生成し、計算結果を詰める
            Intent data = new Intent();
            data.putExtra("result", result);
            setResult(RESULT_OK, data);
        }

        // アクティビティを終了
        finish();
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

        // 計算をし直して結果を表示する
        refreshResult();
    }
}
