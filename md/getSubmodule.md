---
# Gitサブモジュール構成の取得

- 任意のディレクトリを新しく作成し、移動する。(コマンドプロンプト)
```
	＜例＞
	mkdir C:\10_local\project\dev
```

- 最初に親プロジェクト(ここでは「sbwap」)をクローンする。(コマンドはGitBashにて実行する)
```
	git clone [Gitリポジトリ]
	＜例＞
	git clone https://github.com/freedomRemains/sbwap
```

- 親プロジェクト(「sbwap」)のディレクトリ直下に、子プロジェクト(「sblib」)のディレクトリと「.gitmodules」が生成されたことを確認。  
子プロジェクト(「sblib」)のディレクトリはリポジトリ資材、「.gitmodules」はサブモジュールコマンドの内容が書かれている。

- 続けてGitBash上から、次のコマンドを実行する。
```
	cd sbwap
	git submodule init
	git submodule update
```
