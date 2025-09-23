---
# Gitサブモジュール構成の構築

[TOPに戻る](../README.md)

- 任意のディレクトリを新しく作成し、移動する。(コマンドプロンプト)
```
	＜例＞
	mkdir C:\10_local\project\submodule
```

- 最初に親となるプロジェクト(ここでは「sbwap」)をクローンする。(コマンドはGitBashにて実行する)
```
	git clone [Gitリポジトリ]
	＜例＞
	git clone https://github.com/freedomRemains/sbwap
```

- 親プロジェクト(「sbwap」)のディレクトリにてGitBash上から、次のコマンドを実行する。
```
	cd sbwap/
	git submodule add https://github.com/freedomRemains/sblib sblib
	※　子である「sblib」のリポジトリを「sblib」というエイリアス名で、
		サブモジュールとするコマンドを実行。
		git submodule add [リポジトリ] [エイリアス]
```

- 「sbwap」ディレクトリ直下に「sblib」ディレクトリと「.gitmodules」が生成されたことを確認。  
「sblib」ディレクトリはリポジトリ資材、「.gitmodules」はサブモジュールコマンドの内容が書かれている。

- 続けてGitBash上から、次のコマンドを実行する。
```
	git submodule init
	git submodule update
```

- GitBash上から、次のコマンドを実行。
```
	git status
```

- commit&pushを実行する。
```
	git add .
	git commit -m "sblibをsbwapのサブモジュールとして設定。"
	git push origin main
```
