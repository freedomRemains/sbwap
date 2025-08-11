// API呼び出しボタンをクリックしたときのイベントハンドラを指定する
document.getElementById('renderForm').addEventListener('submit', async function(event) {

    // デフォルトの挙動を抑止する(フォーム送信を抑止)
    event.preventDefault();

    // CSRFトークンを取得する
    const csrfToken = document
            .querySelector('meta[name="_csrf"]')
            .getAttribute("content");
    const csrfHeader = document
            .querySelector('meta[name="_csrf_header"]')
            .getAttribute("content");

    // フォームのデータをJSONに変換する
    const form = event.target;
    const formData = new FormData(form);
    const jsonData = {};
    formData.forEach((value, key) => {
        jsonData[key] = value;
    });

    try {
        // APIを呼び出す
        const response = await fetch('/api/v1/render', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        });

        if (!response.ok) {
            throw new Error(`HTTPエラー: ${response.status}`);
        }

        const result = await response.json();
        console.log('APIレスポンス：', result);

        // サーバ側でレンダリングしたHTMLを表示する
        const parser = new DOMParser();
        const newElement = parser.parseFromString(result.html, 'text/html').body.firstElementChild;
        document.getElementById('renderTarget').replaceWith(newElement);

    } catch (error) {

      console.error('送信エラー：', error);
    }
});
