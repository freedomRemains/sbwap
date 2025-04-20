// API呼び出しボタンをクリックしたときのイベントハンドラを指定する
document.getElementById('btnCallApi').addEventListener('click', () => {

    // APIのURLを指定して呼び出し、レスポンスのJSONデータを取得する
    fetch('http://localhost:8080/api/v1/validation', {
            method: 'POST', // POSTメソッドを指定
            headers: {
                'Content-Type': 'application/json' // JSON形式のデータを送信することを指定
            },
            body: JSON.stringify({

                // 送信するデータをJSON形式に変換して指定
                "name": "",
                "childList": [
                    {
                        "childName": "",
                        "grandChildList": [
                            {
                                "grandChildName": ""
                            },
                            {
                                "grandChildName": ""
                            }
                        ]
                    },
                    {
                        "childName": "",
                        "grandChildList": [
                            {
                                "grandChildName": ""
                            },
                            {
                                "grandChildName": ""
                            }
                        ]
                    }
                ],
            })
    })
        .then(response => response.json()) // レスポンスをJSONに変換する
        .then(json => {

            // 取得したデータをHTMLに反映
            document.getElementById('validationArea').innerHTML = `${json.html}`;
        })
        .catch(error => {
            console.error('エラー:', error);
            document.getElementById('validationArea').innerHTML = 'データ取得に失敗しました';
        });
});
