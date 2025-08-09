---
# MinIOの使い方

- 「docker-compose.yml」に、MinIOの起動設定を追加する。

```docker
  # MinIOの設定
  minio:

    # イメージの指定
    image: minio/minio:latest

    # コンテナ名の設定
    container_name: minio

    # ポートの設定
    ports:

      # API用には9000ポート、管理コンソール用には9001ポートを使用
      - "9000:9000"
      - "9001:9001"

    # 環境変数の設定
    environment:

      # MinIOのルートユーザーとパスワードを設定
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin123

    # ボリュームの設定
    volumes:

      # MinIOのデータを永続化するためのボリュームを指定
      - minio_data:/data

    # コマンドの設定
    # MinIOサーバーを起動し、9001ポートで管理コンソールを提供
    command: server /data --console-address ":9001"

  # MinIOのクライアントを使用してバケットを作成するためのサービス
  createbuckets:

    # イメージの指定
    image: minio/mc

    # 当該サービスがMinIOが起動してから実行されるよう、依存関係を設定
    depends_on:
      - minio

    # 起動時にデフォルトのバケットを生成するためのエントリポイント設定
    entrypoint: >
      /bin/sh -c "
        until mc alias set myminio http://minio:9000 minioadmin minioadmin123; do
          echo 'Waiting for MinIO...';
          sleep 2;
        done;
        mc mb --ignore-existing myminio/appstrage;
        exit 0;
      "

# ボリュームの定義
volumes:

  # MinIOのデータを永続化するためのボリューム
  minio_data:
```

- SpringBootでMinIOにアクセスするためには、「build.gradle」に次の設定を追加する。

```Gradle
	// AWS S3(AWS SDK)を使用するための設定
	implementation 'software.amazon.awssdk:s3:2.25.33'
	implementation 'software.amazon.awssdk:auth:2.25.33'
```


- 次のMinIOユーティリティを経由して、アップロード／ダウンロードといった操作を行う。

```Java
```
