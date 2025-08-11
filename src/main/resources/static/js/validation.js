// API呼び出しボタンをクリックしたときのイベントハンドラを指定する
document.getElementById("btnCallApi").addEventListener("click", callApi);

// APIを呼び出す
function callApi() {

    // デフォルトの挙動を抑止する(フォーム送信を抑止)
    event.preventDefault();

    // CSRFトークンを取得する
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");

    // フォームデータを取得する
    const form = document.getElementById("validationForm");
    const formData = new FormData(form);
    const jsonData = {};
    formData.forEach((value, key) => {
        jsonData[key] = value;
    });

    // エラーメッセージをクリアする
    resultMessage.textContent = "";
    errorMessage.value = "";

    // これはAIが提示したサンプル(HTML構成が決まっている場合はこうした書き方で、エラーを一律クリアできる)
    document.querySelectorAll('.error').forEach(e => e.textContent = '');

    fetch("/api/v1/validation", {
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
    .then((response) => response.json()) // レスポンスはJSON形式で返ってくる
    .then((data) => {

        // resultMessageとerrorMessageを取得する
        const resultMessage = document.getElementById("resultMessage");
        const errorMessage = document.getElementById("errorMessage");
        if (data.status === "error") {

            // 画面表示用にエラーメッセージを編集する
            Object.entries(data.errors).forEach(([field, message]) => {
                if (errorMessage) errorMessage.value += message;
            });
            resultMessage.textContent = "バリデーションエラーがあります";

        } else {

            resultMessage.textContent = "登録に成功しました！";
        }
    })
    .catch((err) => {

        console.error(err);
        document.getElementById("resultMessage").textContent = "通信エラーが発生しました";
    });
}
