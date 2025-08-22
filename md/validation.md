---
# バリデーションについて

[TOPに戻る](../README.md)

- はじめに、サンプルではForm及びその子、孫のDTOは「sblib」ではなく「sbwap」に配置している。  
(いずれも画面に付属する個別要素のため、共通ライブラリに入れていない)  
- thymeleafの記述サンプルは、次の通り。  
子や孫が配列になっている複雑なパターンでもバリデーション実施できる。  
なおthymeleafが識別する式の中で配列の添え字を指定するためには、前後をアンダーバー2文字で囲む。  
(エスケープのために必要なコードなので、忘れずに指定すること)

```html
      <div>
        <div>
          <label>親の名前：</label>
          <input type="text" th:field="*{name}" />
          <div th:if="${#fields.hasErrors('name')}" th:errors="*{name}">名前エラー</div>
        </div>

        <div th:each="child, childStat : *{childList}">
          <label>子の名前：</label>
          <input type="text" th:field="*{childList[__${childStat.index}__].childName}" />
          <div th:if="${#fields.hasErrors('childList[' + childStat.index + '].childName')}"
             th:errors="*{childList[__${childStat.index}__].childName}">子エラー</div>

          <div th:each="grand, grandChildStat : *{childList[__${childStat.index}__].grandChildList}">
            <label>孫の名前：</label>
            <input type="text" th:field="*{childList[__${childStat.index}__].grandChildList[__${grandChildStat.index}__].grandChildName}" />
            <div th:if="${#fields.hasErrors('childList[' + childStat.index + '].grandChildList[' + grandChildStat.index + '].grandChildName')}"
               th:errors="*{childList[__${childStat.index}__].grandChildList[__${grandChildStat.index}__].grandChildName}">孫エラー</div>
          </div>
        </div>
```

- Java側では次のように対応する親、子、孫のフォーム及びDTOを用意する。

```java
// 親のフォーム(子のリストには「@Valid」指定が必要)
@Data
public class ValidationForm {

    @NotBlank
    private String name;

    @Valid
    @NotEmpty
    private List<ChildDto> childList;
}
```

```java
// 子のDTO(孫のリストには「@Valid」指定が必要)
@Data
public class ChildDto {

    @NotBlank
    private String childName;

    @Valid
    @NotEmpty
    private List<GrandChildDto> grandChildList;
}
```

```java
// 孫のDTO
@Data
public class GrandChildDto {

    @NotBlank
    private String grandChildName;
}
```

- バリデーションエラーメッセージを定義するためのプロパティファイルに、エラーメッセージを定義する。  
サンプルでは「validation-messages.properties」というプロパティファイルを定義。

```
NotBlank.validationForm.name=バリデーション親DTOの名前を入力してください。
NotBlank.validationForm.childList.childName=子DTOの名前を入力してください。
NotBlank.validationForm.childList.grandChildList.grandChildName=孫DTOの名前を入力してください。

NotEmpty.validationForm.childList=子DTOのリストは必須です。
NotEmpty.validationForm.childList.grandChildList=孫DTOのリストは必須です。

NotBlank=必須項目です。
NotEmpty=本項目の内容を空とすることは、許容されません。
```

- thymeleafとSpringMVCの仕組みを組み合わせてバリデーションを行う場合は、次のようなコードを記述する。
  - SmartValidatorとBindingResultにより、任意のタイミングでバリデーションを実施できる。
  - SmartValidatorを使うことで子以下の階層だけ、孫だけ、といったバリデーションを実施できる。
- 画面にはプロパティファイルで定義したエラーメッセージが表示される。

```java
@Controller
@RequiredArgsConstructor
public class ValidationController {

    /** スマートバリデータ */
    private final SmartValidator validator;

    @PostMapping("/validation")
    public String postValidation(@ModelAttribute ValidationForm validationForm,
            BindingResult bindingResult,
            Model model) {

        // バリデーションエラーがある場合、エラーメッセージを表示するために、再度画面を表示する
        validator.validate(validationForm, bindingResult);
        if (bindingResult.hasErrors()) {

            // エラーがある場合はエラーメッセージを表示するため、再度同じ画面を表示する
            return "validation";
        }

        // 遷移先画面名を呼び出し側に返却する
		return "top";
	}
}
```

- APIでバリデーションを実施する場合は、次のコードを参考とすること。  
サーバ側はRestControllerを準備し、次のようにAPIを定義する。

```java
@RestController
@RequiredArgsConstructor
public class ApiController {

    /** バリデーションユーティリティ */
    private final ValidationUtil validationUtil;

    @PostMapping("/api/v1/validation")
	public ResponseEntity<Map<String, Object>> postValidationByForm(
            @ModelAttribute ValidationForm validationForm,
            BindingResult bindingResult) {

        // バリデーションを実行する
        var errMsgMap = validationUtil.validate(validationForm, bindingResult);
        if (errMsgMap.isEmpty()) {

            // ステータス(正常)をレスポンスに設定し、呼び出し側に返却する
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            return ResponseEntity.ok(response);

        } else {

            // ステータスとエラー情報をレスポンスに設定する
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("errors", errMsgMap);

            // レスポンスを返却する
            return ResponseEntity.badRequest().body(response);
        }
	}
}
```
- API経由とした場合、レスポンスはJSONとする必要がある。(次のコードを参照のこと)  
thymeleafでレンダリングした結果を返却するコードを試したが、エラーとなった。  
(「th:field」などでSpringBoot内部のエラーとなる)  
AIに色々聞いてみたが、問題は解消できなかった。ここでは検証できた方法を記述する。

```java
@RestController
@RequiredArgsConstructor
public class ApiController {

    /** バリデーションユーティリティ */
    private final ValidationUtil validationUtil;

    @PostMapping("/api/v1/validation")
	public ResponseEntity<Map<String, Object>> postValidationByForm(
            @ModelAttribute ValidationForm validationForm,
            BindingResult bindingResult) {

        // バリデーションを実行する
        var errMsgMap = validationUtil.validate(validationForm, bindingResult);
        if (errMsgMap.isEmpty()) {

            // ステータス(正常)をレスポンスに設定し、呼び出し側に返却する
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            return ResponseEntity.ok(response);

        } else {

            // ステータスとエラー情報をレスポンスに設定する
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("errors", errMsgMap);

            // レスポンスを返却する
            return ResponseEntity.badRequest().body(response);
        }
	}
}
```

- バリデーションは、次のユーティリティクラスで実施している。
  - SmartValidatorを使い、任意のタイミングでバリデーションを実施できるようにしている。
  - targetを自由に指定できるようにしている。(孫だけ、子以下の構成だけ、のような指定が可能)
  - 返却するマップはそのままJSONでAPIのクライアント側に返却できるようにしている。
  - 返却するマップのキーは、エラーが起きた位置情報が分かるものとしている。

```java
package com.sb.sblib.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.util.StringUtil;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationUtil {

	/** メッセージソース */
	private final MsgUtil msg;

	/** スマートバリデータ */
	private final SmartValidator smartValidator;

	public Map<String, String> validate(Object target, BindingResult bindingResult) {

		// バリデーションエラーがある場合
		var errMsgMap = new LinkedHashMap<String, String>();
		smartValidator.validate(target, bindingResult);
		if (bindingResult.hasErrors()) {

			// 全てのバリデーションエラーを処理するまでループ
			for (FieldError fieldError : bindingResult.getFieldErrors()) {

				// エラーメッセージマップにエントリを追加する
				putErrMsgMap(fieldError, errMsgMap);
			}
		}

		// エラーメッセージマップを呼び出し側に返却する
		return errMsgMap;
	}

	private void putErrMsgMap(FieldError fieldError, Map<String, String> errMsgMap) {

		// エラーが起きたフィールドのコードを取得する
		String[] codes = fieldError.getCodes();
		if (codes == null) {

			// メッセージリソースにエラーメッセージが定義されていなければ、デフォルトのエラーメッセージを返却する
			errMsgMap.put(fieldError.getField(), fieldError.getDefaultMessage());
			return;
		}

		// どんな場合でも必ず最初のコードをマップのキーとする
		// コードは次のようになっており、エラーが起きた項目の位置情報が最も分かりやすいのは、
		// 必ず先頭のコードとなっている。(少なくともSpringBootがこの仕組みを返るまでは、
		// 必ず先頭のコードをマップのキーとする)
		//   coce[0]  NotBlank.validationForm.childList[0].childName
		//   code[1]  NotBlank.validationForm.childList.childName
		//   code[2]  NotBlank.childList[0].childName
		//   code[3]  NotBlank.childList.childName
		//   code[4]  NotBlank.java.lang.String
		//   code[5]  NotBlank
		String mapKey = codes[0];

		// 全てのコードを処理するまでループ
		var searchedCodes = new StringBuilder();
		for (String code : codes) {
			try {
				// コードでエラーメッセージを取得する
				String errMsg = msg.get(code);
				if (StringUtil.isNotBlank(errMsg)) {

					// エラーメッセージを検知した場合は、マップにエントリを追加して呼び出し側に復帰する
					errMsgMap.put(mapKey, errMsg);
					return;
				} 
			} catch (NoSuchMessageException e) {

				// メッセージリソースに該当メッセージがない場合は、見つからなかった検索キーを記録する
				if (searchedCodes.length() > 0) {
					searchedCodes.append(", ");
				}
				searchedCodes.append(code);
				continue;
			}
		}

		// メッセージリソースにエラーメッセージが定義されていなければ、デフォルトのエラーメッセージを返却する
		log.info(msg.get("validation.noMsg", searchedCodes.toString()));
		errMsgMap.put(fieldError.getField(), fieldError.getDefaultMessage());
		return;
	}
}
```

- クライアント側は次のようなJavaScriptのコードでAPIを呼び出す。
  - サーバ側ではthymeleafと同様にフォームのデータを「@ModelAttribute」で受けている。
  - その場合、クライアントは new FormData(form); をそのままbodyに設定する。

```javascript
// API呼び出しボタンをクリックしたときのイベントハンドラを指定する
document.getElementById('btnCallApi').addEventListener('click', callApi);

// APIを呼び出す
function callApi() {

  // フォームデータを取得する
  const form = document.getElementById('validationForm');
  const formData = new FormData(form);

  // エラーメッセージをクリアする
  resultMessage.textContent = '';
  errorMessage.value = '';

  // これはAIが提示したサンプル(HTML構成が決まっている場合はこうした書き方で、エラーを一律クリアできる)
  //document.querySelectorAll('.error').forEach(e => e.textContent = '');

  fetch('http://localhost:8080/api/v1/validation', {
    method: 'POST',
    body: formData // FormDataをそのまま送信する
  })
  .then(response => response.json()) // レスポンスはJSON形式で返ってくる
  .then(data => {

    // resultMessageとerrorMessageを取得する
    const resultMessage = document.getElementById('resultMessage');
    const errorMessage = document.getElementById('errorMessage');
    if (data.status === 'error') {

      // 画面表示用にエラーメッセージを編集する
      Object.entries(data.errors).forEach(([field, message]) => {
        if (errorMessage)
        errorMessage.value += message;
      });
      resultMessage.textContent = 'バリデーションエラーがあります';
    } else {
      resultMessage.textContent = '登録に成功しました！';
    }
  })
  .catch(err => {
    console.error(err);
    document.getElementById('resultMessage').textContent = '通信エラーが発生しました';
  });
}
```

- HTMLに次のような記述を行い、API呼び出し結果を簡易確認できるようにしている。
  - resultMessageの箇所に結果が表示される。
  - errorMessageのtextareaにバリデーションエラーが表示される。

```html
    <button id="btnCallApi">API呼び出し</button>
    <div id="resultMessage"></div>
    <div>
      <textarea id="errorMessage" rows="10" cols="50" readonly></textarea>
    </div>
```

- なおサーバ側をJSONで受ける場合は、次のコードとする。
  - 「@RequestBody」で受けると、JSON形式での引数受け渡しとなる。
  - 「BindingResult」は「BeanPropertyBindingResult」経由で生成する。

```java
    @PostMapping("/api/v1/validation")
    public ResponseEntity<Map<String, Object>> postValidationByForm(
            @RequestBody ValidationForm validationForm) {

        // バリデーションを実行する
        BindingResult bindingResult = new BeanPropertyBindingResult(validationForm, "validationForm");
        var errMsgMap = validationUtil.validate(validationForm, bindingResult);
```

- JSONで送信する場合、クライアントは次のコードとする。
  - 「Content-Type」を「application/json」としている。
- ただしこのコードだとフォームデータがきちんと渡らなかった。(子要素が空と扱われる)  
コードのデバッグもしくはJSONを手作業で組み立てる必要がある。

```javascript
// API呼び出しボタンをクリックしたときのイベントハンドラを指定する
document.getElementById('btnCallApi').addEventListener('click', callApi);

// APIを呼び出す
function callApi() {

  // フォームデータを取得する
  const form = document.getElementById('validationForm');
  const formData = new FormData(form);
  const jsonData = {};
  formData.forEach((value, key) => {
    jsonData[key] = value;
  });

  // エラーメッセージをクリアする
  resultMessage.textContent = '';
  errorMessage.value = '';

  // これはAIが提示したサンプル(HTML構成が決まっている場合はこうした書き方で、エラーを一律クリアできる)
  //document.querySelectorAll('.error').forEach(e => e.textContent = '');

  fetch('http://localhost:8080/api/v1/validation', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(jsonData)
  })
  .then(response => response.json()) // レスポンスはJSON形式で返ってくる
  .then(data => {

    // resultMessageとerrorMessageを取得する
    const resultMessage = document.getElementById('resultMessage');
    const errorMessage = document.getElementById('errorMessage');
    if (data.status === 'error') {

      // 画面表示用にエラーメッセージを編集する
      Object.entries(data.errors).forEach(([field, message]) => {
        if (errorMessage)
        errorMessage.value += message;
      });
      resultMessage.textContent = 'バリデーションエラーがあります';
    } else {
      resultMessage.textContent = '登録に成功しました！';
    }
  })
  .catch(err => {
    console.error(err);
    document.getElementById('resultMessage').textContent = '通信エラーが発生しました';
  });
}
```
